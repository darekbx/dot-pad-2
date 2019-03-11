package com.dotpad2.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun calculateTimeAgo(createdTime: Long): String {
        val currentTime = Calendar.getInstance().timeInMillis
        val diff = (currentTime - createdTime)
        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "${TimeUnit.MILLISECONDS.toSeconds(diff)}s"
            diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}m"
            diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}h"
            else -> "${TimeUnit.MILLISECONDS.toDays(diff)}d"
        }
    }

    fun requestDateTime(context: Context, result: (calendar: Calendar) -> Unit) {
        showDatePicker(context, { year, month, dayOfMonth ->
            showTimePicker(context, { hourOfDay, minute ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, hourOfDay, minute)
                }
                result(calendar)
            })
        })
    }

    fun showDatePicker(context: Context, date: (year: Int, month: Int, dayOfMonth: Int) -> Unit) {
        DatePickerDialog(context).apply {
            setOnDateSetListener({ view, year, month, dayOfMonth -> date(year, month, dayOfMonth) })
        }.show()
    }

    fun showTimePicker(context: Context, time: (hourOfDay: Int, minute: Int) -> Unit) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { view, hourOfDay, minute -> time(hourOfDay, minute) },
            calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }
}