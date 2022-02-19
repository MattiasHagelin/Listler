package com.math3249.listler.ui.fragment.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddItemArgs(
    val listId: Long = 0,
    val listName: String = "",
    val catName: String = "",
    val itemName:String = ""
): Parcelable
