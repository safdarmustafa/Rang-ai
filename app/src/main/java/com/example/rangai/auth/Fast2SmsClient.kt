package com.example.rangai.auth

import com.example.rangai.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Fast2SmsClient {

    private const val BASE_URL = "https://www.fast2sms.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(Fast2SmsLoggingInterceptor())
        .build()

    val api: Fast2SmsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Fast2SmsApi::class.java)

    val baseUrl: String get() = BASE_URL

    val isApiKeyConfigured: Boolean
        get() {
            val key = BuildConfig.FAST2SMS_API_KEY.trim()
            return key.isNotBlank()
        }
}
