package com.example.rangai.data.util

/**
 * Single source of truth for phone normalization across OTP, DataStore, and Supabase.
 */
object PhoneNormalizer {

    fun normalize(phoneNumber: String): String {
        return phoneNumber.trim().filter { it.isDigit() }
    }

    /**
     * Fast2SMS expects 10-digit Indian mobile numbers without country code.
     * Strips leading 91 when present.
     */
    fun formatForSms(phoneNumber: String): String {
        val digits = normalize(phoneNumber)
        return when {
            digits.length > 10 -> digits.takeLast(10)
            else -> digits
        }
    }
}
