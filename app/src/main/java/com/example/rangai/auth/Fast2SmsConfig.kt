package com.example.rangai.auth

import com.example.rangai.BuildConfig
import com.example.rangai.data.util.PhoneNormalizer

object Fast2SmsConfig {

    val API_KEY: String
        get() = BuildConfig.FAST2SMS_API_KEY.trim()

    /**
     * OTP route — Fast2SMS sends using their pre-approved OTP template.
     * Docs: route=otp, variables_values=<otp digits>
     */
    const val ROUTE_OTP = "otp"

    /**
     * DLT Manual route — requires exact DLT-approved message text.
     * Docs: route=dlt_manual, entity_id (not pe_id), template_id, sender_id
     */
    const val ROUTE_DLT_MANUAL = "dlt_manual"

    /**
     * Switch delivery mode here.
     * - [SmsDeliveryMode.OTP] uses Fast2SMS built-in OTP route (recommended to test first)
     * - [SmsDeliveryMode.DLT_MANUAL] uses your registered DLT template
     */
    val deliveryMode: SmsDeliveryMode = SmsDeliveryMode.OTP

    const val DLT_SENDER_ID = "SPCTEK"

    /** Principal Entity ID — sent as entity_id in API (NOT pe_id). */
    const val DLT_ENTITY_ID = "1201176779722977287"

    const val DLT_TEMPLATE_ID = "1207176874999607244"

    /**
     * Must match your DLT-approved template EXACTLY (only {#var#} replaced).
     * Example approved template: "Your OTP is {#var#} for Rang AI verification."
     * Update this string to match what is registered on the DLT portal.
     */
    fun buildDltMessage(otp: String): String {
        return "Your OTP is $otp for Rang AI verification."
    }

    fun formatPhoneForSms(phoneNumber: String): String {
        return PhoneNormalizer.formatForSms(phoneNumber)
    }
}

enum class SmsDeliveryMode {
    OTP,
    DLT_MANUAL
}
