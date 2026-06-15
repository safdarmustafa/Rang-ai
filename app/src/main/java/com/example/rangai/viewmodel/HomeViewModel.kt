package com.example.rangai.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.rangai.data.repository.StorageRepository

class HomeViewModel : ViewModel() {

    private val storageRepository =
        StorageRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun startEnhancing(
        imageBytes: ByteArray
    ) {
        android.util.Log.d(
            "RANG_AI",
            "startEnhancing called"
        )

        viewModelScope.launch {

            _isLoading.value = true

            try {

                storageRepository.uploadImage(
                    fileName = "test_${System.currentTimeMillis()}.jpg",
                    imageBytes = imageBytes
                )

            } catch (e: Exception) {

                android.util.Log.e(
                    "RANG_AI",
                    "UPLOAD FAILED",
                    e
                )
            } finally {

                _isLoading.value = false
            }
        }
    }

    fun stopEnhancing() {
        _isLoading.value = false
    }
}