package com.example.rangai.data.model

data class ReplicateRequest(
    val input: Input
)

data class Input(
    val image: String,
    val scale: Int
)