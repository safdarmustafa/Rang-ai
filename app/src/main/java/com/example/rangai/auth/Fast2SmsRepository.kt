package com.example.rangai.auth

import android.util.Log
import retrofit2.Response

class Fast2SmsRepository {

    suspend fun sendOtp(
        phoneNumber: String,
        otp: String
    ): Boolean {
        if (!Fast2SmsClient.isApiKeyConfigured) {
            Log.e(TAG, "FAST2SMS_API_KEY is missing in local.properties / BuildConfig")
            return false
        }

        val apiKey = Fast2SmsConfig.API_KEY
        val formattedPhone = Fast2SmsConfig.formatPhoneForSms(phoneNumber)

        if (formattedPhone.length != 10) {
            Log.e(TAG, "Invalid phone for SMS after formatting: raw=$phoneNumber formatted=$formattedPhone")
            return false
        }

        Log.d(TAG, "Preparing SMS — mode=${Fast2SmsConfig.deliveryMode} phone=$formattedPhone otpLength=${otp.length}")
        Log.d(TAG, "Request URL = ${Fast2SmsClient.baseUrl}dev/bulkV2")
        Log.d(TAG, "Authorization = ${maskApiKey(apiKey)}")

        return try {
            val response = when (Fast2SmsConfig.deliveryMode) {
                SmsDeliveryMode.OTP -> sendViaOtpRoute(apiKey, formattedPhone, otp)
                SmsDeliveryMode.DLT_MANUAL -> sendViaDltManualRoute(apiKey, formattedPhone, otp)
            }

            evaluateResponse(response, formattedPhone)
        } catch (e: Exception) {
            Log.e(TAG, "OTP SEND EXCEPTION for phone=$formattedPhone", e)
            false
        }
    }

    private suspend fun sendViaOtpRoute(
        apiKey: String,
        phone: String,
        otp: String
    ): Response<Fast2SmsResponse> {
        Log.d(TAG, "Request Body — route=${Fast2SmsConfig.ROUTE_OTP} variables_values=$otp numbers=$phone")

        return Fast2SmsClient.api.sendOtpRoute(
            authorization = apiKey,
            route = Fast2SmsConfig.ROUTE_OTP,
            variablesValues = otp,
            numbers = phone
        )
    }

    private suspend fun sendViaDltManualRoute(
        apiKey: String,
        phone: String,
        otp: String
    ): Response<Fast2SmsResponse> {
        val message = Fast2SmsConfig.buildDltMessage(otp)

        Log.d(
            TAG,
            "Request Body — route=${Fast2SmsConfig.ROUTE_DLT_MANUAL} " +
                "sender_id=${Fast2SmsConfig.DLT_SENDER_ID} " +
                "entity_id=${Fast2SmsConfig.DLT_ENTITY_ID} " +
                "template_id=${Fast2SmsConfig.DLT_TEMPLATE_ID} " +
                "message=$message numbers=$phone"
        )

        return Fast2SmsClient.api.sendDltManualRoute(
            authorization = apiKey,
            route = Fast2SmsConfig.ROUTE_DLT_MANUAL,
            senderId = Fast2SmsConfig.DLT_SENDER_ID,
            message = message,
            entityId = Fast2SmsConfig.DLT_ENTITY_ID,
            templateId = Fast2SmsConfig.DLT_TEMPLATE_ID,
            numbers = phone
        )
    }

    private fun evaluateResponse(
        response: Response<Fast2SmsResponse>,
        phone: String
    ): Boolean {
        val httpCode = response.code()
        val body = response.body()
        val errorBody = response.errorBody()?.string()

        Log.d(TAG, "Response Status = $httpCode")
        Log.d(TAG, "Response Body (parsed) = $body")
        if (!errorBody.isNullOrBlank()) {
            Log.e(TAG, "Error Body = $errorBody")
        }

        if (!response.isSuccessful) {
            Log.e(
                TAG,
                "HTTP failure for phone=$phone code=$httpCode message=${body?.messageText()} error=$errorBody"
            )
            return false
        }

        if (body == null) {
            Log.e(TAG, "Empty response body for phone=$phone despite HTTP $httpCode")
            return false
        }

        if (!body.isSuccess) {
            Log.e(
                TAG,
                "Fast2SMS rejected SMS for phone=$phone " +
                    "status_code=${body.statusCode} message=${body.messageText()}"
            )
            return false
        }

        Log.d(
            TAG,
            "SMS accepted by Fast2SMS for phone=$phone request_id=${body.requestId}"
        )
        return true
    }

    private fun maskApiKey(apiKey: String): String {
        return if (apiKey.length <= 8) "****" else "${apiKey.take(4)}****${apiKey.takeLast(4)}"
    }

    companion object {
        private const val TAG = "FAST2SMS"
    }
}
