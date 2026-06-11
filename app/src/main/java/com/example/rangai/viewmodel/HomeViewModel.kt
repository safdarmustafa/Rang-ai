package com.example.rangai.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun startEnhancing() {

        viewModelScope.launch {

            _isLoading.value = true

            delay(3000)

            _isLoading.value = false
        }
    }

    fun stopEnhancing() {
        _isLoading.value = false
    }
}