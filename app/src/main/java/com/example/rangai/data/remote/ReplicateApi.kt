package com.example.rangai.data.remote

import com.example.rangai.data.model.ReplicateRequest
import com.example.rangai.data.model.ReplicateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Url

interface ReplicateApi {

    @GET
    suspend fun getPrediction(
        @Url url: String
    ): Response<ReplicateResponse>

    @Headers(
        "Content-Type: application/json" // Type sirf batata hai ki body JSON hai.
    )

    @POST("models/nightmareai/real-esrgan/predictions")
    suspend fun createPrediction(
        @Body request: ReplicateRequest
    ): Response<ReplicateResponse>
}