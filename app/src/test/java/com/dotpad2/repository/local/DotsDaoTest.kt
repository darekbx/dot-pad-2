package com.dotpad2.repository.local

import androidx.room.Room
import com.dotpad2.repository.local.entities.DotDto
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.io.IOException

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class DotsDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dotsDao: DotsDao

    @Before
    fun createDb() {
        db = Room
            .inMemoryDatabaseBuilder(RuntimeEnvironment.application.applicationContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dotsDao = db.getDotsDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun addAndFetch() {
        // Given
        val dot = provideSampleDot()

        // When
        dotsDao.add(dot)

        // Then
        val dots = dotsDao.fetchActive()
        dots.value?.let { list ->
            assertEquals(1, list.size)
            assertEquals("New dot", list[0].text)
        }
    }

    @Test
    fun update() {
        // Given
        val dot = provideSampleDot()
        dotsDao.add(dot)

        // When
        dot.isArchived = true
        dotsDao.update(dot)

        // Then
        val dots = dotsDao.fetchAll(1, 0)
        dots.value?.let { list ->
            assertEquals(true, list[0].isArchived)
        }
    }

    @Test
    fun updateIsArchived() {
        // Given
        val dot = provideSampleDot()
        val id = dotsDao.add(dot)

        // When
        dotsDao.markDotArchived(id, true)

        // Then
        val dots = dotsDao.fetchActive()
        dots.value?.let { list ->
            assertEquals(0, list.size)
        }
    }

    private fun provideSampleDot() = DotDto(null, "New dot", 10, 50, 65, 1000)
}