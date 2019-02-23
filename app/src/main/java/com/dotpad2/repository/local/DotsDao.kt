package com.dotpad2.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dotpad2.repository.local.entities.DotDto

@Dao
interface DotsDao {

    @Insert
    fun add(dotDto: DotDto): Long

    @Query("SELECT * FROM dots WHERE is_archived = 0")
    fun fetchActive(): LiveData<List<DotDto>>

    @Query("SELECT * FROM dots LIMIT :limit OFFSET :offsert")
    fun fetchAll(limit: Int, offsert: Int): LiveData<List<DotDto>>

    @Query("UPDATE dots SET is_archived = :isArchived WHERE id = :dotId")
    fun markDotArchived(dotId: Long, isArchived: Boolean)

    @Update
    fun update(dotDto: DotDto)
}