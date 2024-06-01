package com.example.feeds.dtos

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class MessageDTO(
    val sender_id: String,
    val receiver_id: String,
    val isSent: Boolean,
    val message: String
)
