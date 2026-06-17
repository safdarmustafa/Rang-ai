package com.example.rangai.data.model

data class ReplicateResponse(
    val id: String?,
    val status: String?,
    val output: String?,
    val error: String?,
    val urls: Urls?
)

data class Urls(
    val get: String?
)