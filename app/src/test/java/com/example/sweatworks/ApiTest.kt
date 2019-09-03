package com.example.sweatworks

import com.example.sweatworks.data.api.UsersApi
import com.example.sweatworks.utils.Globals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiTest {

    lateinit var usersApi: UsersApi


    @Before
    fun setup(){

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Globals.BASE_URL)
            .build()
        usersApi = retrofit.create(UsersApi::class.java)
    }

    @Test
    fun shouldFetchFirstPage(){
        usersApi.getUsersByPage(1,100,Globals.API_SEED)
            .test()
            .assertNoErrors()
            .assertNoTimeout()
            .assertValue {
                it.results.size == 100
            }
            .assertValue {
                it.results[0].email.isNotEmpty()
            }
    }

}