package com.example.sweatworks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.sweatworks.data.repositories.UserRepository
import com.example.sweatworks.models.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserDetailViewModel(var userRepository: UserRepository):ViewModel() {

    var disposable = CompositeDisposable()

    fun insertFavUser(user:User):Completable{
        return userRepository.insertUser(user)
    }

    fun getUsersByEmail(userEmail:String):Flowable<List<User>>{
        return userRepository.getUsersByEmail(userEmail)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

class UserDetailViewModelFactory @Inject constructor (var userRepository: UserRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailViewModel(userRepository) as T
    }
}