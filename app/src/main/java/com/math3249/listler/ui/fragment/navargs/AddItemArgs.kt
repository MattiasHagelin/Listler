package com.math3249.listler.ui.fragment.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddItemArgs(
    val listId: Long,
    val listName: String,
    val catId: Long,
    val catName: String,
    val itemId: Long,
    val itemName:String
): Parcelable
