package com.example.sweatworks.di

import android.content.Context
import androidx.room.Room
import com.example.sweatworks.data.api.UsersApi
import com.example.sweatworks.data.daos.UserDao
import com.example.sweatworks.data.db.SweatWorkDb
import com.example.sweatworks.data.repositories.UserRepository
import com.example.sweatworks.data.repositories.UserRepositoryImp
import com.example.sweatworks.utils.Globals
import com.example.sweatworks.viewmodels.UserDetailViewModelFactory
import com.example.sweatworks.viewmodels.UserListViewModelFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun providesContext():Context{
        return context
    }

    @Provides
    @Singleton
    fun providesRetrofit():Retrofit{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(false)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .connectTimeout(10000,TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()
        return  Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Globals.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesUsersApi(retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }


    @Provides
    @Singleton
    fun providesUserRepository(userApi: UsersApi, userDao: UserDao):UserRepository{
        return UserRepositoryImp(userApi, userDao)
    }

    @Provides
    @Singleton
    fun providesUserListViewModelFactory(userRepository: UserRepository):UserListViewModelFactory{
        return UserListViewModelFactory(userRepository)
    }

    @Provides
    @Singleton
    fun providesRoomDatabase(context: Context):SweatWorkDb{
        return Room.databaseBuilder(context,SweatWorkDb::class.java,"sweat_db").build()
    }

    @Provides
    @Singleton
    fun providesUserDao(sweatWorkDb: SweatWorkDb):UserDao{
        return sweatWorkDb.userDao()
    }

    @Provides
    @Singleton
    fun providesUserDetailViewModelFactory(userRepository: UserRepository):UserDetailViewModelFactory{
        return UserDetailViewModelFactory(userRepository)
    }


}