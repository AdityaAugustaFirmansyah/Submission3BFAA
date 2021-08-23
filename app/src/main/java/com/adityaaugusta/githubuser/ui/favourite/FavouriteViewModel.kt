package com.adityaaugusta.githubuser.ui.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.repository.UserFavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private val users: MutableList<User> = mutableListOf()
    private val filterList: MutableList<User> = mutableListOf()
    private var usersState: MutableLiveData<MutableList<User>> = MutableLiveData()
    val repository = UserFavouriteRepository(application)

    fun getUserFavourite(): LiveData<MutableList<User>> {
        viewModelScope.launch {
            for (i in repository.getUserFavourites()) {
                users.add(User(username = i.username, avatar = i.avatar))
            }
            usersState.value = users
        }
        return usersState
    }

    fun searchUserFavourite(username: String) {
        if (username.isNotEmpty()) {
            for (i in users) {
                if (i.username.lowercase().contains(username.lowercase())) {
                    if (!filterList.contains(i)) {
                        filterList.add(i)
                    }
                } else {
                    filterList.remove(i)
                }
            }
            if (filterList.size > 0) {
                usersState.value = filterList
            } else {
                usersState.value = mutableListOf()
            }
        } else {
            usersState.value = users
        }
    }
}