package com.example.rangai.auth

/**
 * Represents the splash screen routing decision.
 *
 * [Loading]     — session is being read from DataStore (show branded splash).
 * [Authenticated] — valid persisted session; route to Home.
 * [Unauthenticated] — no session; route to Login.
 */
sealed interface SplashUiState {

    data object Loading : SplashUiState

    data class Authenticated(
        val phoneNumber: String
    ) : SplashUiState

    data object Unauthenticated : SplashUiState
}
