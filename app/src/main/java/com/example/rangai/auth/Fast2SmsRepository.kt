package com.example.rangai.auth

import android.util.Log

class Fast2SmsRepository {

    suspend fun sendOtp(
        phoneNumber: String,
        otp: String
    ): Boolean {

        return try {

            val response =
                Fast2SmsClient.api.sendOtp(
                    authorization =
                        Fast2SmsConfig.API_KEY,

                    message =
                        "Your Rang AI OTP is $otp",

                    numbers =
                        phoneNumber,

                    senderId =
                        Fast2SmsConfig.DLT_SENDER_ID,

                    peId =
                        Fast2SmsConfig.DLT_PE_ID,

                    templateId =
                        Fast2SmsConfig.DLT_TEMPLATE_ID
                )

            Log.d(
                "FAST2SMS",
                "Code = ${response.code()}"
            )

            response.isSuccessful

        } catch (e: Exception) {

            Log.e(
                "FAST2SMS",
                "OTP FAILED",
                e
            )

            false
        }
    }
}