package com.example.sweatworks.data.repositories

import com.example.sweatworks.data.api.UsersApi
import com.example.sweatworks.data.daos.UserDao
import com.example.sweatworks.models.Result
import com.example.sweatworks.models.User
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import javax.inject.Inject

interface UserRepository {
    fun fetchUsersByPage(page:Int, results:Int, seed:String):Observable<Result>
    fun insertUser(user: User):Completable
    fun getUsersByEmail(userEmail:String):Flowable<List<User>>
    fun getAllUsers():Flowable<List<User>>
}

class UserRepositoryImp @Inject constructor (var usersApi: UsersApi, var userDao: UserDao):UserRepository{

    override fun fetchUsersByPage(page: Int, results: Int, seed: String):Observable<Result> {
        return usersApi.getUsersByPage(page,results,seed)
    }

    override fun insertUser(user: User):Completable {
        return userDao.insert(user)
    }

    override fun getUsersByEmail(userEmail: String): Flowable<List<User>> {
        return userDao.getUserByEmail(userEmail)
    }

    override fun getAllUsers():Flowable<List<User>>{
        return userDao.getAllUsers()
    }
}