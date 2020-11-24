package com.example.pickerimagevideoandfiles.app

import android.app.Application
import com.example.pickerimagevideoandfiles.data.source.local.storage.LocalStorage

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this

        LocalStorage.init(this)
    }

    companion object {
        lateinit var instance: MyApp
    }
}