package com.example.rangai.data.repository

import android.util.Log
import com.example.rangai.data.model.Input
import com.example.rangai.data.model.ReplicateRequest
import com.example.rangai.network.ReplicateClient
import kotlinx.coroutines.delay

class ReplicateRepository {

    suspend fun enhanceImage(
        imageUrl: String,
        scale: Int
    ): String? {

        try {

            val request =
                ReplicateRequest(
                    input = Input(
                        image = imageUrl,
                        scale = scale
                    )
                )

            val createResponse =
                ReplicateClient.api.createPrediction(
                    request
                )

            if (!createResponse.isSuccessful) {
                val errorBody = createResponse.errorBody()?.string()
                Log.e(
                    "RANG_AI",
                    "Replicate create failed — HTTP ${createResponse.code()} body=$errorBody"
                )
                return null
            }

            val prediction =
                createResponse.body()

            Log.d(
                "RANG_AI",
                "HTTP CODE = ${createResponse.code()}"
            )

            Log.d(
                "RANG_AI",
                "Prediction ID = ${prediction?.id}"
            )

            Log.d(
                "RANG_AI",
                "Status = ${prediction?.status}"
            )

            Log.d(
                "RANG_AI",
                "GET URL = ${prediction?.urls?.get}"
            )

            Log.d(
                "RANG_AI",
                "Error = ${prediction?.error}"
            )

            val getUrl =
                prediction?.urls?.get ?: return null

            var status = prediction.status

            while (
                status == "starting" ||
                status == "processing"
            ) {

                delay(3000)

                val resultResponse =
                    ReplicateClient.api.getPrediction(
                        getUrl
                    )

                if (!resultResponse.isSuccessful) {
                    Log.e(
                        "RANG_AI",
                        "Replicate poll failed — HTTP ${resultResponse.code()}"
                    )
                    return null
                }

                val result =
                    resultResponse.body()

                status = result?.status

                Log.d(
                    "RANG_AI",
                    "POLL STATUS = $status"
                )

                Log.d(
                    "RANG_AI",
                    "OUTPUT = ${result?.output}"
                )

                Log.d(
                    "RANG_AI",
                    "ERROR = ${result?.error}"
                )

                if (status == "succeeded") {

                    val enhancedImageUrl = result?.output

                    Log.d(
                        "RANG_AI",
                        "SUCCESS URL = $enhancedImageUrl"
                    )

                    return enhancedImageUrl
                }

                if (status == "failed") {

                    Log.d(
                        "RANG_AI",
                        "FAILED = ${result?.error}"
                    )

                    return null
                }
            }
        } catch (e: Exception) {

            Log.e(
                "RANG_AI",
                "REPLICATE FAILED",
                e
            )
        }
        return null
    }
}
