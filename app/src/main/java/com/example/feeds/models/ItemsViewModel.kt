package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemsViewModel(
    val profileAvatar: String,
    val unreadChatCount: Int,
    val username: String,
    val textMessage: String,
    val textTime: String,
    val senderId: String
)