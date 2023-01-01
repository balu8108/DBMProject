package com.bala.nytnews

import android.app.Application

class NytApplication : Application() {

    override fun onCreate() {
        application = this
        super.onCreate()
    }
    companion object
    {
        lateinit var application: Application
    }
}