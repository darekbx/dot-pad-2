package com.dotpad2.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dotpad2.model.StatisticsValue
import com.dotpad2.repository.local.entities.DotDto

@Dao
interface DotsDao {

    @Insert
    fun addAll(dotDtos: List<DotDto>)

    @Insert
    fun add(dotDto: DotDto): Long

    @Query("SELECT * FROM dots WHERE id = :dotId")
    fun fetchDot(dotId: Long): LiveData<DotDto>

    @Query("SELECT * FROM dots WHERE is_archived = 0 ORDER BY created_date DESC")
    fun fetchActive(): LiveData<List<DotDto>>

    @Query("SELECT * FROM dots WHERE is_archived = 1 ORDER BY created_date DESC LIMIT :limit OFFSET :offset")
    fun fetchArchive(limit: Int, offset: Int): LiveData<List<DotDto>>

    @Query("SELECT * FROM dots WHERE text LIKE :query ORDER BY created_date")
    fun search(query: String): LiveData<List<DotDto>>

    @Query("UPDATE dots SET position_x = :x, position_y = :y WHERE id = :dotId")
    fun updateDotPosition(dotId: Long?, x: Int, y: Int)

    @Query("SELECT COUNT(id) FROM dots")
    fun countStatistics(): LiveData<Int>

    @Query("SELECT COUNT(size) AS occurrences, size AS value FROM dots GROUP BY size")
    fun sizeStatistics(): LiveData<List<StatisticsValue>>

    @Query("SELECT COUNT(color) AS occurrences, color AS value FROM dots GROUP BY color")
    fun colorStatistics(): LiveData<List<StatisticsValue>>

    @Update
    fun update(dotDto: DotDto)

    @Query("UPDATE dots SET color = :newColor WHERE color = :oldColor")
    fun updateColor(newColor: Int, oldColor: Int) : Int
}