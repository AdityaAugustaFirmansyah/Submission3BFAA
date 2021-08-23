package com.adityaaugusta.githubuser.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.adityaaugusta.githubuser.domain.ModelState
import com.adityaaugusta.githubuser.domain.User
import com.adityaaugusta.githubuser.entity.UserFavourite
import com.adityaaugusta.githubuser.repository.UserFavouriteRepository
import com.adityaaugusta.githubuser.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

internal class DetailViewModel(username:String,application: Application) :AndroidViewModel(application) {
    private val repository = UserRepository()
    private val repositoryFavourite= UserFavouriteRepository(application)
    private val state:MutableLiveData<ModelState<User>> = MutableLiveData()

    init {
        state.value = ModelState(true,"", User())
        viewModelScope.launch {
            try {
                val user = repository.getDetailUser(username)
                user?.let {
                    state.value = ModelState(false,"", it)
                }
            }catch (throwable:Throwable){
                when(throwable){
                    is IOException ->{
                        state.value = throwable.message?.let { ModelState(false, it, User()) }
                    }

                    is HttpException ->{
                        val code = throwable.code()
                        val msg = throwable.message()
                        state.value = ModelState(false, "$msg $code", User())
                    }

                    else->{
                        state.value = throwable.message?.let { ModelState(false, it, User()) }
                    }
                }
            }
        }
    }

    fun addFavouriteUser(user: User){
        viewModelScope.launch {
            repositoryFavourite.insertUserFavourite(user)
        }
    }

     fun getUserFavouriteDetail(username:String): LiveData<UserFavourite> {
        return repositoryFavourite.userFavourite(username)
     }

    fun getState():LiveData<ModelState<User>>{
        return state
    }

    fun deleteFavouriteUser(it: User) {
        viewModelScope.launch {
            val favourite = UserFavourite(it.username,it.avatar)
            repositoryFavourite.deleteUserFavourite(favourite)
        }
    }
}