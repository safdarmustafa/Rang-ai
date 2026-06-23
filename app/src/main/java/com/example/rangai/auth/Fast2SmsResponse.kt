package com.example.rangai.auth

import com.google.gson.annotations.SerializedName

/**
 * Fast2SMS bulkV2 JSON response.
 * HTTP 200 can still contain "return": false — always check [isSuccess].
 */
data class Fast2SmsResponse(
    @SerializedName("return")
    val isSuccess: Boolean = false,
    @SerializedName("status_code")
    val statusCode: Int? = null,
    @SerializedName("request_id")
    val requestId: String? = null,
    val message: Any? = null
) {
    fun messageText(): String = when (message) {
        is List<*> -> message.joinToString(", ")
        is String -> message
        else -> message?.toString().orEmpty()
    }
}
