package com.example.feeds.models

data class DataModel(
    var categories: ArrayList<String>,
    var createdAt: String,
    var iconURL: String,
    var id: String,
    var updatedAt: String,
    var url: String,
    var value: String
):java.io.Serializable
