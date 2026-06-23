package com.example.rangai.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.local.DataStoreManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    private val _splashState =
        MutableStateFlow<SplashUiState>(SplashUiState.Loading)

    val splashState: StateFlow<SplashUiState> =
        _splashState.asStateFlow()

    init {
        resolveStartDestination()
    }

    private fun resolveStartDestination() {
        viewModelScope.launch {
            try {
                val sessionDeferred = async {
                    val isLoggedIn = dataStoreManager.isLoggedIn.first()
                    val phoneNumber = dataStoreManager.phoneNumber.first()
                    isLoggedIn to phoneNumber
                }

                delay(SPLASH_MIN_DURATION_MS)

                val (isLoggedIn, phoneNumber) = sessionDeferred.await()

                Log.d(
                    TAG,
                    "Session check — loggedIn=$isLoggedIn, phone=$phoneNumber"
                )

                _splashState.value = when {
                    isLoggedIn && phoneNumber.isNotBlank() ->
                        SplashUiState.Authenticated(phoneNumber)

                    else ->
                        SplashUiState.Unauthenticated
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to read session; defaulting to Login", e)
                _splashState.value = SplashUiState.Unauthenticated
            }
        }
    }

    companion object {
        private const val TAG = "SPLASH_VM"
        private const val SPLASH_MIN_DURATION_MS = 1_000L
    }
}
