package com.dotpad2.model

import android.graphics.Point

class Dot(var text: String,
          var size: Int,
          var color: Int = 0,
          var position: Point,
          var createdDate:Long,
          var isArchived: Boolean,
          var isSticked: Boolean,
          var reminder: Long?,
          var calendarEventId: Long? = null,
          var calendarReminderId: Long? = null
)