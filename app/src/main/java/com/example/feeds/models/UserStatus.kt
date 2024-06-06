package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class UserStatus(
    val id: String?,
    val online: Boolean
)
