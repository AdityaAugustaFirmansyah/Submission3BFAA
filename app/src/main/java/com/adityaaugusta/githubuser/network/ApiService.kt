package com.adityaaugusta.githubuser.network

import com.adityaaugusta.githubuser.network.response.UserNetwork
import com.adityaaugusta.githubuser.network.response.UserNetworkContainer
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers():List<UserNetwork>?

    @GET("search/users")
    suspend fun searchUser(@Query("q")username:String):UserNetworkContainer?

    @GET("users/{username}")
    suspend fun getDetailUsers(@Path("username")username: String):UserNetwork?

    @GET("users/{username}/followers")
    suspend fun getFollowersUsers(@Path("username")username: String):List<UserNetwork>?

    @GET("users/{username}/following")
    suspend fun getFollowingUsers(@Path("username")username: String):List<UserNetwork>?
}