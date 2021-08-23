package com.adityaaugusta.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.adityaaugusta.githubuser.database.UserDatabase
import com.adityaaugusta.githubuser.database.getDatabase
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.entity.UserFavourite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserFavouriteRepository(application:Application) {
    private var database: UserDatabase = getDatabase(application)

    fun userFavourite(username:String): LiveData<UserFavourite> {
        return database.userDao().getUserDetail(username)
    }

    suspend fun insertUserFavourite(user:User){
        val userFavourite = UserFavourite(user.username,user.avatar)
        withContext(Dispatchers.IO){
            database.userDao().insertUserFavourite(userFavourite)
        }
    }

    suspend fun deleteUserFavourite(user:UserFavourite){
        val userFavourite = UserFavourite(username = user.username,avatar = user.avatar)
        withContext(Dispatchers.IO){
            database.userDao().deleteUserFavourite(userFavourite)
        }
    }

    suspend fun getUserFavourites(): List<UserFavourite> {
        return withContext(Dispatchers.IO){database.userDao().getUsers()}
    }
}