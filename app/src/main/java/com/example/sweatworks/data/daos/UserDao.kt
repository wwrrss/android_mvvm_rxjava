package com.example.sweatworks.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sweatworks.models.User
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDao {

    @Insert
    fun insert(user:User):Completable

    @Query("SELECT * FROM users WHERE email = :userEmail")
    fun getUserByEmail(userEmail:String):Flowable<List<User>>

    @Query("SELECT * FROM users")
    fun getAllUsers():Flowable<List<User>>


}