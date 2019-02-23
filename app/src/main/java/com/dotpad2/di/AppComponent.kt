package com.dotpad2.di

import com.dotpad2.ui.dots.DotsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DatabaseModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(activity: DotsActivity)
}