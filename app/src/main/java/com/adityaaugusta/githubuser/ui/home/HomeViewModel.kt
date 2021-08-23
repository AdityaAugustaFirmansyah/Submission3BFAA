package com.adityaaugusta.githubuser.ui.home

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

internal class HomeViewModel : ViewModel() {
    private lateinit var list: List<User>
    private val filterList = mutableListOf<User>()
    private val repository = UserRepository()
    private val state = MutableLiveData<ModelState<List<User>>>()

    init {
        state.value = ModelState(true, "", mutableListOf())
        viewModelScope.launch {
            try {
                list = repository.getUsers()
                state.value = ModelState(false, "", list)
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> {
                        state.value =
                            throwable.message?.let { ModelState(false, it, mutableListOf()) }
                    }

                    is HttpException -> {
                        val code = throwable.code()
                        val msg = throwable.message()
                        state.value = ModelState(false, "$msg $code", mutableListOf())
                    }

                    else -> {
                        state.value =
                            throwable.message?.let { ModelState(false, it, mutableListOf()) }
                    }
                }
            }
        }
    }

    fun getUsers(): LiveData<ModelState<List<User>>> {
        return state
    }

    fun stateSearchUser(username: String, msgEmpty: String) {
        if (username.isNotEmpty()) {
            for (i in list) {
                if (i.username.lowercase().contains(username.lowercase())) {
                    if (!filterList.contains(i)) {
                        filterList.add(i)
                    }
                } else {
                    filterList.remove(i)
                }
            }
            if (filterList.size > 0) {
                state.value = ModelState(false, "", filterList)
            } else {
                state.value = ModelState(false, msgEmpty, mutableListOf())
            }
        } else {
            state.value = ModelState(false, "", list)
        }
    }
}