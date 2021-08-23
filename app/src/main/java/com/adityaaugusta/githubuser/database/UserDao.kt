package com.adityaaugusta.githubuser.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.adityaaugusta.githubuser.entity.UserFavourite

@Dao
interface UserDao {
    @Query("SELECT * FROM UserFavourite Where username = :username")
    fun getUserDetail(username:String):LiveData<UserFavourite>

    @Query("SELECT * FROM UserFavourite")
    fun getUsers():List<UserFavourite>

    @Query("SELECT * FROM UserFavourite")
    fun selectAll():Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserFavourite(userFavourite: UserFavourite)

    @Delete
    fun deleteUserFavourite(userFavourite: UserFavourite)
}