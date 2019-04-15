package com.dotpad2.repository.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dots")
class LegacyDotDto(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "_id") var id: Long? = null,
    @ColumnInfo(name = "text") var text: String = "",
    @ColumnInfo(name = "color") var color: Int = 0,
    @ColumnInfo(name = "size") var size: Int = 0,
    @ColumnInfo(name = "x") var x: Int = 0,
    @ColumnInfo(name = "y") var y: Int = 0,
    @ColumnInfo(name = "date") var date: Long = 0,
    @ColumnInfo(name = "isArchival") var isArchived: Boolean = false,
    @ColumnInfo(name = "reminder") var reminder: Long? = null,
    @ColumnInfo(name = "event_id") var eventId: Long? = null,
    @ColumnInfo(name = "reminder_id") var reminderId: Long? = null,
    @ColumnInfo(name = "is_sticked") var isSticked: Boolean = false
)