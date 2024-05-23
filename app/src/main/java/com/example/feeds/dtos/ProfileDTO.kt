package com.example.feeds.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDTO(
    var user_id: String,
    var username: String
)
