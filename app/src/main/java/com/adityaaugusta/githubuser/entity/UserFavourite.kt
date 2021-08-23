package com.adityaaugusta.githubuser.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserFavourite(
    @PrimaryKey
    val username: String = "",
    val avatar: String = "",
)

const val AUTHORITY = "com.adityaaugusta.githubuser"
const val TABLE_NAME ="UserFavourite"