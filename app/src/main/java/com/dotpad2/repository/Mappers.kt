package com.dotpad2.repository

import android.graphics.Point
import com.dotpad2.model.Dot
import com.dotpad2.repository.local.entities.DotDto

object Mappers {

    fun dtoDtoToDot(dotDto: DotDto) =
        with(dotDto) {
            Dot(
                id ?: null,
                text,
                size,
                color,
                Point(positionX, positionY),
                createdDate,
                isArchived,
                isSticked,
                reminder,
                calendarEventId,
                calendarReminderId
            )
        }

    fun dtoToDotDto(dot: Dot) =
        with(dot) {
            DotDto(
                id,
                text,
                size,
                color,
                position.x,
                position.y,
                createdDate,
                isArchived,
                isSticked,
                reminder,
                calendarEventId,
                calendarReminderId
            )
        }
}