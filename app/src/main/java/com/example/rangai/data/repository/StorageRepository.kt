package com.example.rangai.data.repository

import com.example.rangai.network.SupabaseClient
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.upload

class StorageRepository {

    companion object {
        const val BUCKET_NAME = "rang-ai-images"
    }

    private val supabase = SupabaseClient.client

    private val bucket: BucketApi =
        supabase.storage.from(BUCKET_NAME)

    suspend fun uploadImage(
        fileName: String,
        imageBytes: ByteArray
    ): String {
        android.util.Log.d("RANG_AI", "Uploading started: $fileName")

        try {
            bucket.upload(path = fileName, data = imageBytes) {
                upsert = true
            }
        } catch (e: Exception) {
            val message = e.message.orEmpty()
            android.util.Log.e("RANG_AI", "Upload failed: $message", e)
            if (message.contains("Bucket not found", ignoreCase = true)) {
                throw IllegalStateException(
                    "Supabase bucket \"$BUCKET_NAME\" was not found. " +
                        "Create a public bucket with that name in your Supabase project.",
                    e
                )
            }
            throw e
        }

        android.util.Log.d("RANG_AI", "Upload completed")

        val publicUrl = bucket.publicUrl(fileName)
        android.util.Log.d("RANG_AI", "Public URL: $publicUrl")

        return publicUrl
    }
}
