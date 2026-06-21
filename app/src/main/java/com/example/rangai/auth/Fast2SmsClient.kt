package com.example.rangai.auth

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Fast2SmsClient {

    private val okHttpClient =
        OkHttpClient.Builder()
            .build()

    val api: Fast2SmsApi =
        Retrofit.Builder()
            .baseUrl("https://www.fast2sms.com/")
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(Fast2SmsApi::class.java)
}