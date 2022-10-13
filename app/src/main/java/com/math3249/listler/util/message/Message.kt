package com.math3249.listler.util.message

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Message(
    private var _type: Type = Type.NO_MESSAGE,
    private var _success: Boolean = false,
               /*
               private var _ids: MutableMap<String, Long>? = null,
               private var _data: MutableMap<String, String>? = null,
               private var _extra: String = ""
               */
): Parcelable {
    val success get() = _success
    val type get() = _type
    /*
    private val ids get() = _ids
    private val data get() = _data
    val extra get() = _extra
    */

    open fun clear() {
        _type = Type.NO_MESSAGE
        _success = false

        /*
        _ids?.clear()
        _data?.clear()
        _extra = ""
        */
    }

    enum class Type {
        STORE_INSERTED,
        STORE_NEW,
        LIST_INSERTED,
        LIST_SORT_ORDER,
        LIST_HOME_SCREEN,
        NEW_LIST,
        CATEGORY_IN_DATABASE,
        CATEGORY_INPUT_EMPTY,
        ITEM_CATEGORY_NEW,
        ITEM_NEW,
        ITEM_NEW_DEFAULT,
        ITEM_IN_LIST,
        ITEM_NOT_IN_DATABASE,
        ITEM_INSERTED,
        ITEM_UPDATED,
        ITEM_DELETED,
        ITEM_IN_DATABASE,
        ITEM_INPUT_EMPTY,
        ITEM_MISSING_CATEGORY,
        INVALID_INPUT,
        DELETE,
        NO_MESSAGE;
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

