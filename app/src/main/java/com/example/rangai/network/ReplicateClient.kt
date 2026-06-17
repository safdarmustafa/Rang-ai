package com.example.rangai.network

import com.example.rangai.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.rangai.data.remote.ReplicateApi

object ReplicateClient {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request: Request =
                chain.request()
                    .newBuilder()
                    .addHeader(
                        "Authorization",
                        "Token ${BuildConfig.REPLICATE_API_KEY}"
                    )
                    .build()

            chain.proceed(request)
        }
        .build()

    val api: ReplicateApi =
        Retrofit.Builder()
            .baseUrl("https://api.replicate.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ReplicateApi::class.java)
}