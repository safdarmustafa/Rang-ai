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

    private val _uploadedImageUrl =
        MutableStateFlow<String?>(null)

    val uploadedImageUrl: StateFlow<String?> =
        _uploadedImageUrl

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

                val imageUrl =
                    storageRepository.uploadImage(
                        fileName = "test_${System.currentTimeMillis()}.jpg",
                        imageBytes = imageBytes
                    )

                _uploadedImageUrl.value = imageUrl

                android.util.Log.d(
                    "RANG_AI",
                    "IMAGE URL = $imageUrl"
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