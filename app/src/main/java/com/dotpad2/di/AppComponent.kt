package com.dotpad2.di

import com.dotpad2.di.viewmodels.ViewModelModule
import com.dotpad2.dotdialog.DotDialog
import com.dotpad2.dots.DotsActivity
import com.dotpad2.dots.list.DotsListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class, DatabaseModule::class, ViewModelModule::class))
interface AppComponent {

    fun inject(activity: DotsActivity)
    fun inject(dotDialog: DotDialog)
    fun inject(listFragment: DotsListFragment)
}