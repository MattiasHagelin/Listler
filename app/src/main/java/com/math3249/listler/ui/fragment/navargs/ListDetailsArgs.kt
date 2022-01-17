package com.math3249.listler.ui.fragment.navargs

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ListDetailsArgs(
    val listId: Long,
    val listName: String
): Parcelable
