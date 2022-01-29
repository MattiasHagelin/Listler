package com.math3249.listler.util.message

import android.os.Parcelable
import com.math3249.listler.model.entity.Store
import kotlinx.parcelize.Parcelize

@Parcelize
class StoreMessage(
    private var _type: Type = Type.NO_MESSAGE,
    private var _success: Boolean = false,
    private var _store: Store

): Message(_type, _success), Parcelable {
    val store: Store = _store

    override fun clear() {
        super.clear()
        _store = Store()
    }
}