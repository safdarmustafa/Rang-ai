package com.example.rangai.auth

/**
 * One-shot result after OTP submission.
 * NavGraph observes this to decide Home vs Profile Setup.
 */
sealed interface OtpAuthState {

    data object Idle : OtpAuthState

    data object Loading : OtpAuthState

    data object InvalidOtp : OtpAuthState

    /** User already registered in Supabase — go to Home. */
    data object ExistingUser : OtpAuthState

    /** No Supabase profile yet — go to Profile Setup. */
    data object NewUser : OtpAuthState

    data class Error(val message: String) : OtpAuthState
}
