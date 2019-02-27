package com.dotpad2.di

import android.content.Context
import com.dotpad2.DotPadApplication
import com.dotpad2.repository.Repository
import com.dotpad2.repository.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val application: DotPadApplication) {

    @Provides
    @Singleton
    fun provideApplication(): DotPadApplication = application

    @Provides
    fun provideContext(): Context = application

    @Provides
    fun provideRepository(appDatabase: AppDatabase): Repository =
        Repository(appDatabase.getDotsDao())
}