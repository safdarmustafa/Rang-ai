package com.example.rangai.auth

object SmsOtpParser {

  private val OTP_REGEX = Regex("""\b(\d{6})\b""")

  fun extractOtp(message: String?): String? {
    if (message.isNullOrBlank()) return null
    return OTP_REGEX.find(message)?.groupValues?.get(1)
  }
}
