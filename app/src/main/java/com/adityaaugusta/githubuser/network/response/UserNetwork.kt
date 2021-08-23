package com.adityaaugusta.githubuser.network.response

import com.adityaaugusta.githubuser.domain.User


data class UserNetworkContainer(
    val total_count:Int=0,
    val items:List<UserNetwork> = mutableListOf()
)
data class UserNetwork (
    val login:String? = "",
    val avatar_url:String? = "",
    val name:String? = "",
    val company:String? = "",
    val location:String? = "",
    val followers:Int = 0,
    val following:Int = 0,
    val public_repos:Int = 0,
)

fun UserNetworkContainer.asUsers(): List<User> {
    return items.map {
        User(username = it.login.toString(),avatar = it.avatar_url.toString())
    }
}

fun UserNetwork.asUser(): User {
    return User(avatar = avatar_url.toString(),username = login ?: "",name = name ?: "",company = company?:"",location = location?:"",
    follower = followers,following = following,repository = public_repos)
}