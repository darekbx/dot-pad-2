package com.dotpad2.ui.dot

import android.content.ContentValues
import com.dotpad2.model.Dot
import android.provider.CalendarContract.Events
import android.annotation.SuppressLint
import android.content.Context
import android.provider.CalendarContract
import com.dotpad2.repository.LocalPreferences
import java.util.*
import java.util.concurrent.TimeUnit
import android.provider.CalendarContract.Reminders
import android.content.ContentUris
import android.net.Uri

@SuppressLint("MissingPermission")
class DotReminder(val context: Context, val localPreferences: LocalPreferences) {

    companion object {
        val NOTIFICATION_LIFETIME = TimeUnit.HOURS.toMillis(4)
        val CALENDAR_EVENTS_URI = "content://com.android.calendar/events/"
        val CALENDAR_REMINDERS_URI = "content://com.android.calendar/reminders//"
    }

    fun addReminder(dot: Dot): Pair<Long, Long> {
        val eventId = createEvent(dot)
        val reminderId = createReminder(eventId)
        return Pair(eventId, reminderId)
    }

    fun removeReminder(dot: Dot) {
        contentResolver.delete(
            ContentUris.withAppendedId(
                Uri.parse(CALENDAR_EVENTS_URI), dot.calendarEventId ?: 0
            ), null, null
        )
        contentResolver.delete(
            ContentUris.withAppendedId(
                Uri.parse(CALENDAR_REMINDERS_URI), dot.calendarReminderId ?: 0
            ), null, null
        )
    }

    private fun prepareEventValues(dot: Dot): ContentValues {
        val calendarId = fetchCalendarId()
        val reminderTime = dot.reminder ?: 0L
        val eventContentValues = ContentValues().apply {
            put(Events.DTSTART, reminderTime)
            put(Events.DTEND, reminderTime + NOTIFICATION_LIFETIME)
            put(Events.TITLE, dot.text)
            put(Events.DESCRIPTION, dot.text)
            put(Events.EVENT_COLOR, dot.color)
            put(Events.CALENDAR_ID, calendarId)
            put(Events.HAS_ALARM, true)
            put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getDisplayName())
        }
        return eventContentValues
    }

    private fun createEvent(dot: Dot): Long {
        val eventContentValues = prepareEventValues(dot)
        val createdEventUri = contentResolver.insert(
            CalendarContract.Events.CONTENT_URI,
            eventContentValues
        )
        return createdEventUri!!.lastPathSegment!!.toLong()
    }

    private fun prepareReminderValues(eventId: Long): ContentValues {
        val reminderContentValues = ContentValues().apply {
            put(Reminders.EVENT_ID, eventId)
            put(Reminders.METHOD, Reminders.METHOD_ALERT)
            put(Reminders.MINUTES, 0)
        }
        return reminderContentValues
    }

    private fun createReminder(eventId: Long): Long {
        val reminderContentValues = prepareReminderValues(eventId)
        val createdReminderUri = contentResolver.insert(
            CalendarContract.Reminders.CONTENT_URI,
            reminderContentValues
        )
        return createdReminderUri!!.lastPathSegment!!.toLong()
    }

    private fun fetchCalendarId(): Long? {
        var calendarId: Long? = null

        val userEmail = localPreferences.getEmailAddress()
        val cursor = contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            arrayOf(CalendarContract.Calendars._ID),
            "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.CALENDAR_DISPLAY_NAME} = ?",
            arrayOf(userEmail, userEmail),
            null
        )

        cursor?.run {
            moveToFirst()
            calendarId = getLong(0)
            close()
        }

        return calendarId
    }

    private val contentResolver by lazy { context.contentResolver }
}
