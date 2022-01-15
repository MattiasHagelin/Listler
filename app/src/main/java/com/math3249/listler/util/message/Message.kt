package com.math3249.listler.util.message

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.math3249.listler.util.message.type.MessageType

class Message (private var _type: MessageType = MessageType.READ_MESSAGE,
               private var _success: Boolean = false,
               private var _ids: MutableMap<String, Long>? = null,
               private var _extra: String = ""
){
    private var _read = _type == MessageType.READ_MESSAGE
    val type get() = _type
    val ids get() = _ids
    val extra get() = _extra
    val read get() = _read

    fun clear() {
        _type = MessageType.READ_MESSAGE
        _success = false
        _ids?.clear()
        _extra = ""
        _read = true
    }

    fun getIdsKeys(): List<String>? {
        return ids?.keys?.toList()
    }

    fun getId(key: String): Long{
        return ids?.get(key) ?: -1
    }

    companion object {
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
    }
}
