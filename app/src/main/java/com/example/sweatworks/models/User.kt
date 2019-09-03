package com.example.sweatworks.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "users")
data class User(
                @PrimaryKey
                @ColumnInfo(name = "email")
                var email:String,
                @Embedded
                var picture: Picture,
                @Embedded
                var name: Name,
                var isSaved:Boolean = false,
                var phone:String
):Serializable