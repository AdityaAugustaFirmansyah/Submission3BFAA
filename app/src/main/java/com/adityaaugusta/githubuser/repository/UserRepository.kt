package com.adityaaugusta.githubuser.repository

import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.network.ApiClient
import com.adityaaugusta.githubuser.network.response.asUser
import com.adityaaugusta.githubuser.network.response.asUsers

class UserRepository {

    suspend fun searchUser(userName:String):List<User>{
        val listUser = mutableListOf<User>()
        val users = ApiClient.service.searchUser(userName)
        users?.let {
            listUser.addAll(it.asUsers())
        }
        return listUser
    }

    suspend fun getFollowersUser(userName: String): List<User> {
        val listUser = mutableListOf<User>()
        val users = ApiClient.service.getFollowersUsers(userName)
        users?.let {
            for (i in it){
                listUser.add(i.asUser())
            }
        }
        return listUser
    }

    suspend fun getFollowingUser(userName: String): List<User> {
        val listUser = mutableListOf<User>()
        val users = ApiClient.service.getFollowingUsers(userName)
        users?.let {
            for (i in it){
                listUser.add(i.asUser())
            }
        }
        return listUser
    }

    suspend fun getUsers(): List<User> {
        val listUser = mutableListOf<User>()
        val users = ApiClient.service.getUsers()
        users?.let {
            for (i in it){
                listUser.add(i.asUser())
            }
        }
        return listUser
    }

    suspend fun getDetailUser(userName:String): User? {
        val user = ApiClient.service.getDetailUsers(userName)
        return user?.asUser()
    }
}