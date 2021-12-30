package com.math3249.listler.util.message

import com.math3249.listler.util.message.Type.MessageType

class Message (private var _type: MessageType,
                    private var _success: Boolean,
                    private var _listId: Long,
                    private var _categoryId: Long,
                    private var _itemId: Long,
                    private var _extra: String = ""
){
    private var _messageRead = false
    val type get() = _type
    val success get() = _success
    val listId get() = _listId
    val categoryId get() = _categoryId
    val itemId get() = _itemId
    val extra get() = _extra
    val messageRead get() = false

    fun clear() {
        _type = MessageType.READ_MESSAGE
        _success = false
        _listId = -1
        _categoryId = -1
        _itemId = -1
        _extra = ""
        _messageRead = true
    }
}
