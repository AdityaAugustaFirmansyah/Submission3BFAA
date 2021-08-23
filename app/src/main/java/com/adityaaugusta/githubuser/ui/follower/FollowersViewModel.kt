package com.adityaaugusta.githubuser.ui.follower

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adityaaugusta.githubuser.domain.ModelState
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

internal class FollowersViewModel(private val username:String) : ViewModel() {
    private val repository = UserRepository()
    private val state = MutableLiveData<ModelState<List<User>>>()

    init {
        state.value = ModelState(true, "", mutableListOf())
        viewModelScope.launch {
            try {
                val list = repository.getFollowersUser(username)
                state.value = ModelState(false, "", list)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException ->{
                        state.value = throwable.message?.let { ModelState(false, it, mutableListOf()) }
                    }

                    is HttpException ->{
                        val code = throwable.code()
                        val msg = throwable.message()
                        state.value = ModelState(false, "$msg $code", mutableListOf())
                    }

                    else->{
                        state.value = throwable.message?.let { ModelState(false, it, mutableListOf()) }
                    }
                }
            }
        }
    }

    fun getState():LiveData<ModelState<List<User>>>{
        return state
    }
}