package com.example.rangai.ui.profile

import com.example.rangai.data.model.User

sealed interface ProfileUiState {

    data object Loading : ProfileUiState

    data class Success(
        val user: User,
        val phoneNumber: String
    ) : ProfileUiState

    data class Error(
        val message: String
    ) : ProfileUiState
}
