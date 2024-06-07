package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(

    val id: String,
    val created_at: String,
    val user_id: String,
    val username: String,
    val profile_avatar_url: String
)