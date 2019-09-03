package com.example.sweatworks.di

import com.example.sweatworks.views.MainActivity
import com.example.sweatworks.views.UserDetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class) )
interface AppComponent {
    fun inject(target:MainActivity)
    fun inject(target:UserDetailActivity)
}