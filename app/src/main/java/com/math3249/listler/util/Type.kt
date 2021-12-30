package com.math3249.listler.util

import com.math3249.listler.R

enum class Type {
    SHOPPING_LIST{
        override fun toString(): String {
            return StringUtil.getString(R.string.shopping_list)
        }
    },
    MEDICINE_LIST {
        override fun toString(): String {
            return StringUtil.getString(R.string.medicine_list)
        }
    };
}