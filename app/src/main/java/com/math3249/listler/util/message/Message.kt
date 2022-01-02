package com.math3249.listler.util.message

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.math3249.listler.ui.fragment.AddListFragmentDirections
import com.math3249.listler.util.message.Type.MessageType

class Message (private var _type: MessageType = MessageType.READ_MESSAGE,
                    private var _success: Boolean,
                    private var _listId: Long,
                    private var _categoryId: Long,
                    private var _itemId: Long,
                    private var _extra: String = ""
){
    private var _messageRead = _type == MessageType.READ_MESSAGE
    val type get() = _type
    val success get() = _success
    val listId get() = _listId
    val categoryId get() = _categoryId
    val itemId get() = _itemId
    val extra get() = _extra
    val messageRead get() = _messageRead

    fun clear() {
        _type = MessageType.READ_MESSAGE
        _success = false
        _listId = -1
        _categoryId = -1
        _itemId = -1
        _extra = ""
        _messageRead = true
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
