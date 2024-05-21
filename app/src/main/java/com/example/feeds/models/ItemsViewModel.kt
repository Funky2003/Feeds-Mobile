package com.example.feeds.models

data class ItemsViewModel(
    val profileAvatar: Int,
    val unreadChatCount: Int,
    val username: String,
    val textMessage: String,
    val textTime: String
    )