package com.adityaaugusta.githubuser.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adityaaugusta.githubuser.ui.detail.DetailViewModel
import com.adityaaugusta.githubuser.ui.favourite.FavouriteViewModel
import com.adityaaugusta.githubuser.ui.follower.FollowersViewModel
import com.adityaaugusta.githubuser.ui.following.FollowingViewModel
import com.adityaaugusta.githubuser.ui.search.SearchViewModel
import java.lang.IllegalArgumentException

class UsernameViewModelFactory(private val username:String,private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(username,application) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(username,application) as T
            }
            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(username) as T
            }
            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(username) as T
            }
            modelClass.isAssignableFrom(FavouriteViewModel::class.java) -> {
                FavouriteViewModel(application) as T
            }
            else -> throw IllegalArgumentException()
        }
    }
}