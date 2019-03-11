package com.dotpad2.model

import android.graphics.Point
import java.text.SimpleDateFormat
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

    companion object {
        val DATE_FORMAT = "yyyy-MM-dd HH:mm"
    }

    fun formattedDate() = dateFormatter.format(Date(createdDate))

    val dateFormatter by lazy { SimpleDateFormat(DATE_FORMAT) }

    val hasReminder: Boolean
        get() = reminder != null
}