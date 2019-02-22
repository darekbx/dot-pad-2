package com.dotpad2.di

import android.content.Context
import com.dotpad2.DotPadApplication
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

}