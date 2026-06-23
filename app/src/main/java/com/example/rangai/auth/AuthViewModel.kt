package com.example.rangai.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.local.DataStoreManager
import com.example.rangai.data.repository.UserRepository
import com.example.rangai.data.util.PhoneNormalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val fast2SmsRepository = Fast2SmsRepository()
    private val userRepository = UserRepository()
    private val dataStoreManager = DataStoreManager(application)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _otpSent = MutableStateFlow(false)
    val otpSent: StateFlow<Boolean> = _otpSent.asStateFlow()

    private val _otpAuthState = MutableStateFlow<OtpAuthState>(OtpAuthState.Idle)
    val otpAuthState: StateFlow<OtpAuthState> = _otpAuthState.asStateFlow()

    private var generatedOtp = ""
    private var phoneNumber = ""

    fun sendOtp(phone: String) {
        phoneNumber = normalizePhone(phone)
        Log.d(TAG, "sendOtp — normalized phone = $phoneNumber")

        generatedOtp = (100000..999999).random().toString()
        Log.d(TAG, "Generated OTP = $generatedOtp")

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = fast2SmsRepository.sendOtp(
                    phoneNumber = phoneNumber,
                    otp = generatedOtp
                )
                _otpSent.value = success
                if (success) {
                    Log.d(TAG, "OTP Sent = true")
                } else {
                    Log.e(TAG, "OTP Sent = false — SMS was NOT delivered. Check FAST2SMS logs.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "OTP SEND FAILED", e)
                _otpSent.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onOtpSubmitted(enteredOtp: String) {
        if (_otpAuthState.value == OtpAuthState.Loading) {
            Log.w(TAG, "onOtpSubmitted ignored — already loading")
            return
        }

        if (!isOtpValid(enteredOtp)) {
            _otpAuthState.value = OtpAuthState.InvalidOtp
            Log.d(TAG, "Invalid OTP entered")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _otpAuthState.value = OtpAuthState.Loading

            try {
                dataStoreManager.saveSession(phoneNumber)
                Log.d(TAG, "Session saved for phone = $phoneNumber")

                Log.d(TAG, "Checking user with phone = $phoneNumber")
                val existingUser = userRepository.getUserByPhone(phoneNumber)
                Log.d(TAG, "Existing user = $existingUser")

                _otpAuthState.value = if (existingUser != null) {
                    Log.d(TAG, "Routing to Home")
                    OtpAuthState.ExistingUser
                } else {
                    Log.d(TAG, "Routing to Profile Setup")
                    OtpAuthState.NewUser
                }
            } catch (e: Exception) {
                Log.e(TAG, "Post-OTP flow failed for phone = $phoneNumber", e)
                _otpAuthState.value = OtpAuthState.Error(
                    message = "Something went wrong. Please try again."
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetOtpAuthState() {
        _otpAuthState.value = OtpAuthState.Idle
    }

    fun logout(onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Logout — clearing DataStore session")
                dataStoreManager.clearSession()
                generatedOtp = ""
                phoneNumber = ""
                _otpSent.value = false
                _otpAuthState.value = OtpAuthState.Idle
                Log.d(TAG, "Logout — session and phone number cleared")
            } catch (e: Exception) {
                Log.e(TAG, "Logout failed", e)
            } finally {
                onComplete()
            }
        }
    }

    fun getPhoneNumber(): String = phoneNumber

    private fun isOtpValid(enteredOtp: String): Boolean {
        return enteredOtp.trim() == generatedOtp.trim()
    }

    private fun normalizePhone(phone: String): String {
        return PhoneNormalizer.normalize(phone)
    }
}

private const val TAG = "AUTH"
