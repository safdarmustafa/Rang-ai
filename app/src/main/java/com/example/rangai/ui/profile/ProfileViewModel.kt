package com.example.rangai.ui.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.local.DataStoreManager
import com.example.rangai.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)
    private val userRepository = UserRepository()

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            Log.d(TAG, "Profile loading started")

            try {
                val isLoggedIn = dataStoreManager.isLoggedIn.first()
                val phoneNumber = dataStoreManager.phoneNumber.first()

                Log.d(
                    TAG,
                    "Session check — isLoggedIn=$isLoggedIn, phone=$phoneNumber"
                )

                if (!isLoggedIn || phoneNumber.isBlank()) {
                    Log.w(TAG, "Session invalid — cannot load profile")
                    _uiState.value = ProfileUiState.Error(
                        message = "Session expired. Please log in again."
                    )
                    return@launch
                }

                Log.d(TAG, "User fetch — querying Supabase for phone=$phoneNumber")
                val user = userRepository.getUserByPhone(phoneNumber)
                Log.d(TAG, "User fetch result = $user")

                _uiState.value = if (user != null) {
                    ProfileUiState.Success(user = user, phoneNumber = phoneNumber)
                } else {
                    ProfileUiState.Error(
                        message = "Could not load your profile. Please try again."
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Profile loading failed", e)
                _uiState.value = ProfileUiState.Error(
                    message = "Something went wrong while loading your profile."
                )
            }
        }
    }
}

private const val TAG = "PROFILE"
