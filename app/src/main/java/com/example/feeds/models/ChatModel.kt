package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatModel(
    val id: String,
    val created_at: String,
    val sender_id: String,
    val receiver_id: String,
    val isSent: Boolean,
    val message: String
)
