package com.dotpad2.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dotpad2.repository.local.entities.DotDto

@Database(entities = arrayOf(DotDto::class), version = 2, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val DB_NAME = "dots_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX index_dots_is_archived ON dots(is_archived)")
            }
        }
    }

    abstract fun getDotsDao(): DotsDao
}