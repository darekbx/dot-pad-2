package com.dotpad2.repository

import android.graphics.Point
import com.dotpad2.model.Dot
import com.dotpad2.repository.local.entities.DotDto
import com.dotpad2.repository.local.entities.LegacyDotDto

object Mappers {

    fun dtoDtoToDot(dotDto: DotDto) =
        with(dotDto) {
            Dot(
                id,
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

    fun dotDtoToLegacyDot(dotDto: DotDto) =
        with(dotDto) {
            LegacyDotDto(
                null,
                text,
                color,
                size,
                positionX,
                positionY,
                createdDate,
                isArchived,
                reminder,
                calendarEventId,
                calendarReminderId,
                isSticked
            )
        }
}