package com.example.sweatworks.utils

import android.app.Application
import com.example.sweatworks.di.AppComponent
import com.example.sweatworks.di.AppModule
import com.example.sweatworks.di.DaggerAppComponent

class SweatApp :Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }
}