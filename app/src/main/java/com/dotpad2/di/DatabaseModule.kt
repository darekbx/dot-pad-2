package com.dotpad2.di

import android.content.Context
import androidx.room.Room
import com.dotpad2.repository.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context) =
            Room
                .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DB_NAME)
                .build()
}