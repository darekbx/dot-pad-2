package com.dotpad2.model

import androidx.room.ColumnInfo

class StatisticsValue(
    @ColumnInfo(name = "occurrences") var occurrences: Int?,
    @ColumnInfo(name = "value") var value: Int?
)