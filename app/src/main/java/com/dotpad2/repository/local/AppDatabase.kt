package com.dotpad2.repository.local

import android.content.Context
import android.os.Environment
import android.os.FileUtils
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dotpad2.repository.local.entities.DotDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Database(entities = arrayOf(DotDto::class), version = 2, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    companion object {
        val DB_NAME = "dots_db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE INDEX index_dots_is_archived ON dots(is_archived)")
            }
        }

        fun makeBackup(context: Context, callback: (path: String?) -> Unit) {
            try {
                val currentDate =
                    SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().timeInMillis)
                val outFile = File("/storage/emulated/0/Download/", "dotpad_db_$currentDate.sqlite")
                //outFile.createNewFile()

                val databaseFile = context.getDatabasePath(DB_NAME)
                CoroutineScope(Dispatchers.IO).launch {
                    databaseFile.copyTo(outFile)
                    callback(outFile.path)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    abstract fun getDotsDao(): DotsDao
}
