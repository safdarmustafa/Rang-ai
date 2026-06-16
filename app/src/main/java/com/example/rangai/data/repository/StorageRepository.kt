package com.example.rangai.data.repository

import com.example.rangai.network.SupabaseClient
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload

class StorageRepository {

    private val supabase =
        SupabaseClient.client

    private val bucket: BucketApi =
        supabase.storage.from("rang-ai-images")

    suspend fun uploadImage(
        fileName: String,
        imageBytes: ByteArray
    ): String {

        android.util.Log.d(
            "RANG_AI",
            "Uploading started: $fileName"
        )

        bucket.upload(
            path = fileName,
            data = imageBytes
        )

        android.util.Log.d(
            "RANG_AI",
            "Upload completed"
        )

        val publicUrl =
            bucket.publicUrl(fileName)

        android.util.Log.d(
            "RANG_AI",
            "Public URL: $publicUrl"
        )

        return publicUrl
    }
}