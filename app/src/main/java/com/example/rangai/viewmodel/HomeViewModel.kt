package com.example.rangai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.repository.ReplicateRepository
import com.example.rangai.data.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val storageRepository =
        StorageRepository()

    private val replicateRepository =
        ReplicateRepository()

    private val _isLoading =
        MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> =
        _isLoading

    private val _uploadedImageUrl =
        MutableStateFlow<String?>(null)

    val uploadedImageUrl: StateFlow<String?> =
        _uploadedImageUrl

    private val _enhancedImageUrl =
        MutableStateFlow<String?>(null)

    val enhancedImageUrl: StateFlow<String?> =
        _enhancedImageUrl

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

                _uploadedImageUrl.value =
                    imageUrl

                android.util.Log.d(
                    "RANG_AI",
                    "IMAGE URL = $imageUrl"
                )

                android.util.Log.d(
                    "RANG_AI",
                    "Calling Replicate..."
                )

                val enhancedUrl =
                    replicateRepository.enhanceImage(
                        imageUrl
                    )

                _enhancedImageUrl.value =
                    enhancedUrl

                android.util.Log.d(
                    "RANG_AI",
                    "VIEWMODEL URL = $enhancedUrl"
                )

            } catch (e: Exception) {

                android.util.Log.e(
                    "RANG_AI",
                    "PROCESS FAILED",
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
    fun clearEnhancedImage() {

        _enhancedImageUrl.value = null

    }
}
