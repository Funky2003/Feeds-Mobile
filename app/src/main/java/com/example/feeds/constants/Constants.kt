package com.example.feeds.constants

class Constants (
    private val chuckNorrisUrl: String = "https://api.chucknorris.io",
) {
    fun getBaseUrl() : String {
        return this.chuckNorrisUrl
    }
}