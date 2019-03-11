package com.dotpad2.repository

import android.content.Context
import com.dotpad2.DotPadApplication

class LocalPreferences(context: Context) {

    companion object {
        val EMAIL_ADDRESS_KEY = "email_address"
    }

    fun saveEmailAddress(email: String) {
        preferences.edit().putString(EMAIL_ADDRESS_KEY, email).apply()
    }

    fun getEmailAddress() = preferences.getString(EMAIL_ADDRESS_KEY, null)

    private val preferences by lazy {
        context.getSharedPreferences(
            DotPadApplication::class.java.simpleName, Context.MODE_PRIVATE
        )
    }
}