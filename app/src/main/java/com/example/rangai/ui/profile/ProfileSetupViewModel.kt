package com.example.rangai.ui.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.model.User
import com.example.rangai.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileSetupViewModel : ViewModel() {

    private val repository = UserRepository()

    fun saveUser(
        phoneNumber: String,
        name: String,
        age: Int
    ) {
        viewModelScope.launch {
            val success = repository.saveUser(
                User(
                    phone_number = phoneNumber,
                    name = name,
                    age = age
                )
            )
            Log.d(TAG, "Profile setup save — success=$success phone=$phoneNumber")
        }
    }
}

private const val TAG = "PROFILE_SETUP"
