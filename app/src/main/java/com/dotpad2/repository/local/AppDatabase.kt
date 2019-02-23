package com.dotpad2.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dotpad2.repository.local.entities.DotDto

@Database(entities = arrayOf(DotDto::class), version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val DB_NAME = "dots_db"
    }

    abstract fun getDotsDao(): DotsDao
}