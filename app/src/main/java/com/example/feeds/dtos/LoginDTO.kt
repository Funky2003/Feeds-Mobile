package com.example.feeds.dtos

import kotlinx.serialization.Serializable

@Serializable
class LoginDTO(
    private var email: String,
    private var password: String,
) {

    fun getEmail() : String {
        return this.email
    }

    fun getPassword() : String {
        return this.password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setEmail(email: String) {
        this.email = email
    }

    override fun toString(): String {
        return "LoginDTO(email='$email', password='$password')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginDTO

        if (email != other.email) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}