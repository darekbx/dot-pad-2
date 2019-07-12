package com.dotpad2.di

import android.content.Context
import android.os.Environment
import androidx.room.Room
import com.dotpad2.repository.local.AppDatabase
import com.dotpad2.repository.local.LegacyAppDatabase
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
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()

    @Singleton
    @Provides
    fun provideLegacyAppDatabase(context: Context): LegacyAppDatabase {
        val dir = Environment.getExternalStorageDirectory()
        val dbPath = "$dir/dotpad.sqlite"
        return Room
            .databaseBuilder(context, LegacyAppDatabase::class.java, dbPath)
            .build()
    }
}