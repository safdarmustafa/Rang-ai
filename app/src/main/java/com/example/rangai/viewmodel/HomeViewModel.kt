package com.example.rangai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rangai.data.repository.ReplicateRepository
import com.example.rangai.data.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val storageRepository = StorageRepository()
    private val replicateRepository = ReplicateRepository()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _uploadedImageUrl = MutableStateFlow<String?>(null)
    val uploadedImageUrl: StateFlow<String?> = _uploadedImageUrl.asStateFlow()

    private val _enhancedImageUrl = MutableStateFlow<String?>(null)
    val enhancedImageUrl: StateFlow<String?> = _enhancedImageUrl.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun startEnhancing(
        imageBytes: ByteArray,
        scale: Int
    ) {
        android.util.Log.d("RANG_AI", "startEnhancing called — scale=$scale bytes=${imageBytes.size}")

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val imageUrl = storageRepository.uploadImage(
                    fileName = "test_${System.currentTimeMillis()}.jpg",
                    imageBytes = imageBytes
                )
                _uploadedImageUrl.value = imageUrl
                android.util.Log.d("RANG_AI", "IMAGE URL = $imageUrl")

                android.util.Log.d("RANG_AI", "Calling Replicate...")
                val enhancedUrl = replicateRepository.enhanceImage(
                    imageUrl = imageUrl,
                    scale = scale
                )

                if (enhancedUrl.isNullOrBlank()) {
                    _errorMessage.value =
                        "Enhancement failed. Check Replicate API key, credits, and Logcat tag RANG_AI."
                    android.util.Log.e("RANG_AI", "Replicate returned no image URL")
                    return@launch
                }

                _enhancedImageUrl.value = enhancedUrl
                android.util.Log.d("RANG_AI", "VIEWMODEL URL = $enhancedUrl")
            } catch (e: Exception) {
                android.util.Log.e("RANG_AI", "PROCESS FAILED", e)
                _errorMessage.value = e.message ?: "Something went wrong while enhancing the image."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun reportError(message: String) {
        _errorMessage.value = message
    }

    fun clearEnhancedImage() {
        _enhancedImageUrl.value = null
    }
}
