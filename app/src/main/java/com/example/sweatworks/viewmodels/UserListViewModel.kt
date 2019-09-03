package com.example.sweatworks.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sweatworks.data.repositories.UserRepository
import com.example.sweatworks.models.Result
import com.example.sweatworks.models.User
import com.example.sweatworks.utils.Globals
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UserListViewModel constructor(var userRepository: UserRepository):ViewModel(){

    var usersObservable = BehaviorSubject.create<Pair<List<User>,Int>>()

    var userList:MutableList<User> = mutableListOf()

    var disposables = CompositeDisposable()

    var currentPage = 1;

    var isLoadingData = false;

    init {
        //Load initial data
        loadMore()
    }
    fun loadMore(){
        getUsersByPage(currentPage,100)
    }
    fun getUsersByPage(page:Int, results:Int){
        if(isLoadingData) return;
        isLoadingData = true;
        val d =userRepository.fetchUsersByPage(page,results,Globals.API_SEED)
            .subscribeOn(Schedulers.io())
            .retry(2)
            .subscribe({
                userList.addAll(it.results.toMutableList())
                usersObservable.onNext(Pair(userList,currentPage))
                isLoadingData=false;
                currentPage++;

            },{
                it.toString()
                isLoadingData=false;
            })
        disposables.add(d)
    }

    fun getAllUsers():Flowable<List<User>>{
        return userRepository.getAllUsers()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

class UserListViewModelFactory @Inject constructor(var userRepository: UserRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserListViewModel(userRepository) as T
    }
}