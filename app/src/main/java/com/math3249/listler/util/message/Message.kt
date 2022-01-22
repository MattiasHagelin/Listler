package com.math3249.listler.util.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Message (private var _type: Type? = null,
               private var _success: Boolean = false,
               private var _ids: MutableMap<String, Long>? = null,
               private var _data: MutableMap<String, String>? = null,
               private var _extra: String = ""
): Parcelable {
    val success get() = _success
    val type get() = _type
    val ids get() = _ids
    val data get() = _data
    val extra get() = _extra

    fun clear() {
        _type = null
        _success = false
        _ids?.clear()
        _extra = ""
    }

    fun getIdsKeys(): List<String>? {
        return ids?.keys?.toList()
    }

    fun getId(key: String): Long{
        return ids?.get(key) ?: -1
    }

    fun getData(key: String): String {
        return data?.get(key) ?: ""
    }

    enum class Type {
        STORE_INSERTED,
        STORE_NEW,
        LIST_INSERTED,
        NEW_LIST,
        CATEGORY_IN_DATABASE,
        CATEGORY_INPUT_EMPTY,
        ITEM_IN_LIST,
        ITEM_NOT_IN_DATABASE,
        ITEM_INSERTED,
        ITEM_IN_DATABASE,
        ITEM_INPUT_EMPTY,
        ITEM_MISSING_CATEGORY,
        INVALID_INPUT,
        DELETE;
}
/*
        fun redirectMessage(type: MessageType,
                            action: NavDirections,
                            navController: NavController) {
            if (type != MessageType.READ_MESSAGE) {
                when (type) {
                    MessageType.LIST_INSERTED -> {
                        navController.navigate(action)
                    }
                    MessageType.ITEM_INSERTED -> {
                        navController.navigate(action)
                    }
                    else -> {

                    }
                }
            }
        }
*/
}

