package com.dotpad2.model

import android.graphics.Point
import com.dotpad2.utils.TimeUtils
import java.util.*

class Dot(
    var id: Long? = null,
    var text: String = "",
    var size: Int = 0,
    var color: Int = 0,
    var position: Point = Point(),
    var createdDate: Long = Calendar.getInstance().timeInMillis,
    var isArchived: Boolean = false,
    var isSticked: Boolean = false,
    var reminder: Long? = null,
    var calendarEventId: Long? = null,
    var calendarReminderId: Long? = null
) {

    fun formattedDate() = TimeUtils.formattedDate(createdDate)

    fun resetReminder() {
        reminder = null
        calendarEventId = null
        calendarReminderId = null
    }

    val hasReminder: Boolean
        get() = reminder != null
}