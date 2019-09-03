package com.example.sweatworks.data.api

import com.example.sweatworks.models.Result
import com.example.sweatworks.models.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {

    @GET("api/")
    fun getUsersByPage(@Query("page")page:Int,@Query("results") results:Int,@Query("seed") seed:String)
            :Observable<Result>

}