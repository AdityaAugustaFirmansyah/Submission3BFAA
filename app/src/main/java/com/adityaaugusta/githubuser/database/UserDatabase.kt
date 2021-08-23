package com.adityaaugusta.githubuser.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adityaaugusta.githubuser.entity.UserFavourite

@Database(entities = [UserFavourite::class],version = 1)
abstract class UserDatabase :RoomDatabase(){
    abstract fun userDao():UserDao
}

private lateinit var INSTANCE:UserDatabase

fun getDatabase(application: Application):UserDatabase {
    synchronized(UserDatabase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(application.applicationContext,UserDatabase::class.java,"user_favourite")
                .build()
        }
    }
    return INSTANCE
}