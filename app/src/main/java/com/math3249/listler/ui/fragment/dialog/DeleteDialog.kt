package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.math3249.listler.R
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.Message.Type

class DeleteDialog(
    private val item: String = "",
    private val id: Long = 0,
    private val position: Long
    ): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.confirm_deletion, item))
            .setPositiveButton(StringUtil.getString(R.string.btn_delete)) { _, _->
                setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to Message(
                    Type.DELETE,
                    true,
                    mutableMapOf(ITEM_ID to id, POSITION to position)
                )))
            }.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    companion object {
        const val TAG = "DeleteDialog"
    }
}