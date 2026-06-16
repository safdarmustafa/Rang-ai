package com.example.rangai.data.remote

import com.example.rangai.data.model.ReplicateRequest
import com.example.rangai.data.model.ReplicateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ReplicateApi {

    @Headers(
        "Content-Type: application/json"
    )
    @POST("predictions")
    suspend fun createPrediction(
        @Body request: ReplicateRequest
    ): Response<ReplicateResponse>
}