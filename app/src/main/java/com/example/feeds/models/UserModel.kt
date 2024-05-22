package com.example.feeds.models

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    private var username: String,
    private var email: String,
    private var password: String,
    private var phone: String
) {
//    constructor() : this("", "", "")
    fun getUsername() : String {
        return this.username
    }

    fun getEmail() : String {
        return this.email
    }

    fun getPassword() : String {
        return this.password
    }

    fun getPhone() : String {
        return this.phone
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setPhone(phone: String) {
        this.phone = phone
    }

    override fun toString(): String {
        return "UserModel(username='$username', email='$email', password='$password', phone='$phone')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (username != other.username) return false
        if (email != other.email) return false
        if (password != other.password) return false
        if (phone != other.phone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + phone.hashCode()
        return result
    }
}
