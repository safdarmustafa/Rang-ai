package com.example.rangai.network

import com.example.rangai.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain
    ): Response {

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "Authorization",
                "Bearer ${BuildConfig.REPLICATE_API_KEY}"
            )
            .build()

        return chain.proceed(request)
    }
}