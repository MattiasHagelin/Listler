package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.math3249.listler.R
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.util.KEY_INPUT
import com.math3249.listler.util.KEY_REQUEST
import com.math3249.listler.util.POSITION
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.message.type.ListData

class DeleteDialog(
    private val listItem: ListCategoryItem = ListCategoryItem(),
    private val position: Int = -1
    ): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.confirm_deletion, listItem.itemName))
            .setPositiveButton(StringUtil.getString(R.string.btn_delete)) { _, _ ->
                    setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to ListMessage(
                        Type.DELETE,
                        true,
                        ListData(listItem = listItem)
                    ),
                    POSITION to position))
            }.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    companion object {
        const val TAG = "DeleteDialog"
    }
}