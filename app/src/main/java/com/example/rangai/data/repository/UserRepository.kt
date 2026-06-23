package com.example.rangai.data.repository

import android.util.Log
import com.example.rangai.data.model.User
import com.example.rangai.data.util.PhoneNormalizer
import com.example.rangai.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class UserRepository {

    suspend fun saveUser(user: User): Boolean {
        val normalizedPhone = normalizePhone(user.phone_number)

        return try {
            if (userExists(normalizedPhone)) {
                Log.d(TAG, "User already exists — skipping insert: $normalizedPhone")
                return true
            }

            SupabaseClient.client
                .postgrest
                .from(TABLE_USERS)
                .insert(
                    user.copy(
                        phone_number = normalizedPhone,
                        name = user.name,
                        age = user.age
                    )
                )

            Log.d(TAG, "User saved successfully: $normalizedPhone")
            true
        } catch (e: Exception) {
            if (isDuplicateKeyError(e)) {
                Log.w(
                    TAG,
                    "Duplicate key on insert — user already exists: $normalizedPhone",
                    e
                )
                return true
            }

            Log.e(
                TAG,
                "INSERT ERROR for phone = $normalizedPhone",
                e
            )
            false
        }
    }

    suspend fun getUserByPhone(phoneNumber: String): User? {
        val normalizedPhone = normalizePhone(phoneNumber)
        if (normalizedPhone.isBlank()) {
            Log.w(TAG, "getUserByPhone called with blank phone after normalization")
            return null
        }

        return try {
            Log.d(TAG, "Searching phone = $normalizedPhone")

            val users = fetchUsersForPhone(normalizedPhone)

            Log.d(TAG, "Users found = ${users.size}")
            Log.d(TAG, "First user = ${users.firstOrNull()}")

            users.firstOrNull()
        } catch (e: Exception) {
            Log.e(
                TAG,
                "FETCH ERROR for phone = $normalizedPhone",
                e
            )
            null
        }
    }

    suspend fun userExists(phoneNumber: String): Boolean {
        return getUserByPhone(phoneNumber) != null
    }

    /**
     * Tries string filter first, then numeric filter.
     * Handles text vs bigint phone_number columns in Supabase.
     */
    private suspend fun fetchUsersForPhone(normalizedPhone: String): List<User> {
        val stringMatch = queryUsersByPhoneFilter(normalizedPhone)
        if (stringMatch.isNotEmpty()) return stringMatch

        val phoneAsLong = normalizedPhone.toLongOrNull()
        if (phoneAsLong != null) {
            Log.d(TAG, "String filter returned 0 rows — retrying with numeric phone = $phoneAsLong")
            val numericMatch = queryUsersByPhoneFilter(phoneAsLong)
            if (numericMatch.isNotEmpty()) return numericMatch
        }

        return emptyList()
    }

    private suspend fun queryUsersByPhoneFilter(phoneFilter: Any): List<User> {
        val response = SupabaseClient.client
            .postgrest
            .from(TABLE_USERS)
            .select(Columns.list("phone_number", "name", "age")) {
                filter {
                    eq("phone_number", phoneFilter)
                }
            }

        Log.d(TAG, "Raw response (filter=$phoneFilter) = ${response.data}")

        return try {
            response.decodeList<User>()
        } catch (e: Exception) {
            Log.e(
                TAG,
                "DECODE ERROR for filter=$phoneFilter raw=${response.data}",
                e
            )
            emptyList()
        }
    }

    private fun normalizePhone(phoneNumber: String): String {
        return PhoneNormalizer.normalize(phoneNumber)
    }

    private fun isDuplicateKeyError(e: Exception): Boolean {
        val message = e.message.orEmpty().lowercase()
        return message.contains("duplicate key") ||
            message.contains("users_phone_number_key") ||
            message.contains("unique constraint")
    }

    companion object {
        private const val TAG = "USER_REPO"
        private const val TABLE_USERS = "users"
    }
}
