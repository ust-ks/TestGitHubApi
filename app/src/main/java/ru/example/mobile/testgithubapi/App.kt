package ru.example.mobile.testgithubapi

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DI.setupKoin()
    }
}