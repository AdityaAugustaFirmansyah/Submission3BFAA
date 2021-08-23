package com.adityaaugusta.githubuser.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String = "",
    val name: String = "",
    val avatar: String = "",
    val company: String = "",
    val location: String = "",
    val repository: Int = 0,
    val favourite: Boolean = false,
    val follower: Int = 0,
    val following: Int = 0
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false
        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }
}

data class ModelState<T>(
    val loading: Boolean,
    val msg: String,
    val success: T
)