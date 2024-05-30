package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(

    val id: String,
    val created_at: String,
    val user_id: String,
    val username: String
)

//val id: String,
//val profile_id: String,
//val content: String,
//val created_at: String,
//val recipient_id: String