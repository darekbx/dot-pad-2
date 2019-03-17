package com.dotpad2.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.dotpad2.repository.local.entities.DotDto

@Dao
interface LegacyDotsDao {

    @Query("""SELECT
               _id AS id,
               text,
               size,
               color,
               x AS position_x,
               y AS position_y,
               date AS created_date,
               isArchival AS is_archived,
               is_sticked,
               reminder,
               event_id AS calendar_event_id,
               reminder_id AS calendar_reminder_id
             FROM dots
             WHERE date > :startDate""")
    fun fetchAll(startDate: Long): LiveData<List<DotDto>>
}