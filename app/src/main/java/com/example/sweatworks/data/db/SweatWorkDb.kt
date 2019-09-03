package com.example.sweatworks.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sweatworks.data.daos.UserDao
import com.example.sweatworks.models.User

@Database(entities = arrayOf(User::class),version = 1)
abstract class SweatWorkDb:RoomDatabase() {
    abstract fun userDao():UserDao
}