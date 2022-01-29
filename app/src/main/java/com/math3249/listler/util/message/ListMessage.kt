package com.math3249.listler.util.message

import android.os.Parcelable
import com.math3249.listler.util.message.type.ListData
import kotlinx.parcelize.Parcelize

@Parcelize
class ListMessage(
    private var _type: Type = Type.NO_MESSAGE,
    private var _success: Boolean = false,
    private var _listData: ListData = ListData()
): Message(_type, _success), Parcelable {

    val listData get() = _listData

    override fun clear() {
        super.clear()
        _listData = ListData()
    }
}