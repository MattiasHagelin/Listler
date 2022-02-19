package com.math3249.listler.util

import androidx.annotation.StringRes
import com.math3249.listler.App

object StringUtil {
    /**
     * Get string from string resource file
     */
    fun getString(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }

    fun validateUserInput(input: String?): Boolean {
        return !input.isNullOrBlank()
    }

    /**
     * Sets first letter to upper case
     * and the rest to lower case
     */
    fun standardizeItemName(name: String): String {
        val n = name.trim()
        return if (n.isBlank()) {
            ""
        } else {
            n.lowercase()
                .replaceFirstChar { it.uppercaseChar() }
        }
    }
}