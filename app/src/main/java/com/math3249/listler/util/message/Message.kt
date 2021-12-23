package com.math3249.listler.util.message

import com.math3249.listler.util.message.Type.MessageType

data class Message (val type: MessageType,
                    val success: Boolean,
                    val itemId: Long
)
