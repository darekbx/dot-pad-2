package com.dotpad2

import android.app.Application
import com.dotpad2.di.AppComponent
import com.dotpad2.di.AppModule
import com.dotpad2.di.DaggerAppComponent

class DotPadApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()

    }
}