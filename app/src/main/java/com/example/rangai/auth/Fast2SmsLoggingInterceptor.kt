package com.example.rangai.auth

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

/**
 * Logs Fast2SMS HTTP traffic with API key masked.
 */
internal class Fast2SmsLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val maskedAuth = maskApiKey(request.header("authorization"))
        Log.d(TAG, "Request URL = ${request.url}")
        Log.d(TAG, "Request Method = ${request.method}")
        Log.d(TAG, "Request Headers authorization = $maskedAuth")

        request.body?.let { body ->
            val buffer = Buffer()
            body.writeTo(buffer)
            Log.d(TAG, "Request Body = ${buffer.readUtf8()}")
        }

        val response = chain.proceed(request)
        val rawBody = response.peekBody(MAX_LOG_BYTES).string()

        Log.d(TAG, "Response Status = ${response.code}")
        Log.d(TAG, "Response Body = $rawBody")

        return response
    }

    private fun maskApiKey(apiKey: String?): String {
        if (apiKey.isNullOrBlank()) return "<missing>"
        return if (apiKey.length <= 8) "****" else "${apiKey.take(4)}****${apiKey.takeLast(4)}"
    }

    companion object {
        private const val TAG = "FAST2SMS"
        private const val MAX_LOG_BYTES = 1024L * 1024L
    }
}
