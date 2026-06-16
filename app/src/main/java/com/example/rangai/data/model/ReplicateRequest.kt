package com.example.rangai.data.model

data class ReplicateRequest(
    val version: String,
    val input: Input
)

data class Input(
    val image: String
)