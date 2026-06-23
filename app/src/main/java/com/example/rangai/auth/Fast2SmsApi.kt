package com.example.rangai.auth

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface Fast2SmsApi {

    /**
     * Fast2SMS OTP route — variables_values carries the OTP digits only.
     * https://www.fast2sms.com/otp-sms/
     */
    @FormUrlEncoded
    @POST("dev/bulkV2")
    suspend fun sendOtpRoute(
        @Header("authorization") authorization: String,
        @Field("route") route: String,
        @Field("variables_values") variablesValues: String,
        @Field("numbers") numbers: String
    ): Response<Fast2SmsResponse>

    /**
     * Fast2SMS DLT Manual route — message must match DLT-approved text exactly.
     * https://docs.fast2sms.com/reference/dlt-manual-single
     */
    @FormUrlEncoded
    @POST("dev/bulkV2")
    suspend fun sendDltManualRoute(
        @Header("authorization") authorization: String,
        @Field("route") route: String,
        @Field("sender_id") senderId: String,
        @Field("message") message: String,
        @Field("entity_id") entityId: String,
        @Field("template_id") templateId: String,
        @Field("numbers") numbers: String
    ): Response<Fast2SmsResponse>

    /** Escape hatch for debugging — logs full form body via interceptor. */
    @FormUrlEncoded
    @POST("dev/bulkV2")
    suspend fun sendRaw(
        @Header("authorization") authorization: String,
        @FieldMap fields: Map<String, String>
    ): Response<Fast2SmsResponse>
}
