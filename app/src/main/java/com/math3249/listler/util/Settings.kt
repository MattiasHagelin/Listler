package com.math3249.listler.util

import android.content.Context
import androidx.preference.PreferenceManager

/**
 * Created this class to make it easier to access shared preferences
 * Also got exceptions when trying to invoke the get functions in SharedPreferences
 */

class Settings(context: Context) {

    private val settings = PreferenceManager.getDefaultSharedPreferences(context)

    fun getInt(key: String, defValue: Int = 0): Int {
        return settings.getString(key,defValue.toString())?.toIntOrNull() ?: (defValue)
    }

}
