package com.adityaaugusta.consumerapp.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String = "",
    val name: String= "",
    val avatar: String= "",
    val company: String= "",
    val location: String= "",
    val repository: Int = 0,
    val favourite: Boolean = false,
    val follower: Int = 0,
    val following: Int = 0
) : Parcelable