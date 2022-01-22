package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.math3249.listler.R
import com.math3249.listler.util.KEY_INPUT
import com.math3249.listler.util.KEY_REQUEST
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.Message

class AddStoreDialog(): DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val layout = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_input, null)
        val inputEditText = layout.findViewById<TextInputEditText>(R.id.dialog_input)
        layout.findViewById<TextInputLayout>(R.id.dialog_label).hint = getString(R.string.hint_store_name)

        builder.setView(layout)
            .setTitle(getString(R.string.title_new_store))
            .setPositiveButton(StringUtil.getString(R.string.btn_add_store)) { _, _->
                val input = StringUtil.standardizeItemName(
                    inputEditText.text.toString())
                if (input == null)
                    setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to Message(
                        Message.Type.INVALID_INPUT,
                        false
                    )))
                else
                    setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to Message(
                        Message.Type.STORE_NEW,
                        true,
                        _extra = input
                    )))
            }.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    companion object {
        const val TAG = "AddStoreDialog"
    }
}