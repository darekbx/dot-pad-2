package com.dotpad2.model

import android.graphics.Point
import java.util.*

class Dot(
    var id: Long? = null,
    var text: String,
    var size: Int,
    var color: Int = 0,
    var position: Point,
    var createdDate:Long = Calendar.getInstance().timeInMillis,
    var isArchived: Boolean = false,
    var isSticked: Boolean = false,
    var reminder: Long? = null,
    var calendarEventId: Long? = null,
    var calendarReminderId: Long? = null
)