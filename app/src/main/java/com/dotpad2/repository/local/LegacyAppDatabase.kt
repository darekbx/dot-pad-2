package com.dotpad2.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dotpad2.repository.local.entities.LegacyDotDto

@Database(entities = arrayOf(LegacyDotDto::class), version = 1, exportSchema = true)
abstract class LegacyAppDatabase: RoomDatabase() {

    abstract fun getLegacyDotsDao(): LegacyDotsDao
}