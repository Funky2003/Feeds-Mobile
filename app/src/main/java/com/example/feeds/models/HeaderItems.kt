package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class HeaderItems (
    val user_id: String,
    val username: String,
)