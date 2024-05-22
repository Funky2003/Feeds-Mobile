package com.example.feeds.repositories

interface AuthenticationRepository {
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signInWithGoogle(): Boolean
}