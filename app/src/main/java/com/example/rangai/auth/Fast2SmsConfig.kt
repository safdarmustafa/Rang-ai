package com.example.rangai.auth

import com.example.rangai.BuildConfig

object Fast2SmsConfig {

    val API_KEY: String
        get() = BuildConfig.FAST2SMS_API_KEY.trim()

    const val DLT_SENDER_ID = "SPCTEK"

    const val DLT_PE_ID =
        "1201176779722977287"

    const val DLT_TEMPLATE_ID =
        "1207176874999607244"
}