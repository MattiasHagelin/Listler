package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.math3249.listler.R
import com.math3249.listler.util.KEY_INPUT
import com.math3249.listler.util.KEY_REQUEST
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.message.type.ListData

class AddListDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val layout = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_list, null)
        val inputEditText = layout.findViewById<TextInputEditText>(R.id.dialog_input)
        layout.findViewById<TextInputLayout>(R.id.dialog_label).hint = getString(R.string.hint_name_list)


        val dropDown = layout.findViewById<AutoCompleteTextView>(R.id.list_type_dropdown)
        prepareListTypeDropDown(dropDown)

        builder.setView(layout)
            .setTitle(getString(R.string.title_new_list))
            .setPositiveButton(StringUtil.getString(R.string.btn_add_list)) { _, _->
                val input = StringUtil.standardizeItemName(
                    inputEditText.text.toString())
                val type = StringUtil.standardizeItemName(dropDown.text.toString())
                if (input == null || type == null)
                    setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to Message(
                        Type.INVALID_INPUT,
                        false
                    )))
                else
                    setFragmentResult(KEY_REQUEST, bundleOf(KEY_INPUT to ListMessage(
                        Type.NEW_LIST,
                        true,
                        ListData(input, )
                    )))
            }.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    private fun prepareListTypeDropDown(
        dropDown: AutoCompleteTextView
    ) {
        //Create a string array from enum of list types
        val types = enumValues<com.math3249.listler.util.Type>()
        val typesAsString = arrayOfNulls<String>(types.count())
        for (i in types.indices) {
            typesAsString[i] = types[i].toString()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_dropdown, typesAsString)
        dropDown.setAdapter(adapter)
        dropDown.setText(typesAsString[0], false)
    }

    companion object {
        const val TAG = "AddListDialog"
    }
}