package com.example.feeds.models

class HeaderItems (

    private var name: String,
    private var profileAvatar: Int
) {
    constructor() : this(name = "", profileAvatar = 0)

    fun setProfileAvatar(avatar: Int) {
        this.profileAvatar = avatar
    }

    fun getProfileAvatar() : Int {
        return this.profileAvatar
    }

    fun setName(nName: String) {
        this.name = nName
    }

    fun getName(): String {
        return this.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HeaderItems

        if (name != other.name) return false
        if (profileAvatar != other.profileAvatar) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + profileAvatar
        return result
    }

    override fun toString(): String {
        return "HeaderItems(name='$name', profileAvatar=$profileAvatar)"
    }
}