package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class DataModel(
    var categories: ArrayList<String>,
    var createdAt: String,
    var iconURL: String,
    var id: String,
    var updatedAt: String,
    var url: String,
    var value: String
)
