package com.math3249.listler.util.dialogs

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
import com.math3249.listler.util.*
import com.math3249.listler.util.message.type.MessageType

class AddListDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val layout = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_list, null)
        val inputEditText = layout.findViewById<TextInputEditText>(R.id.dialog_input)
        layout.findViewById<TextInputLayout>(R.id.dialog_label).hint = getString(R.string.name_list_hint)


        val dropDown = layout.findViewById<AutoCompleteTextView>(R.id.list_type_dropdown)
        prepareListTypeDropDown(dropDown)

        builder.setView(layout)
            .setTitle(getString(R.string.add_new_list))
            .setPositiveButton(StringUtil.getString(R.string.add_new_list)) { _, _->
                val input = StringUtil.standardizeItemName(
                    inputEditText.text.toString())
                val type = StringUtil.standardizeItemName(dropDown.text.toString())
                if (input == null || type == null || input.trim() == "" || type.trim() == "")
                    Utils.snackbar(MessageType.INVALID_INPUT, layout)
                else
                    setFragmentResult(REQUEST_KEY, bundleOf(INPUT_KEY to input, KEY_LIST_TYPE to type))
            }.setNegativeButton(StringUtil.getString(R.string.cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    private fun prepareListTypeDropDown(
        dropDown: AutoCompleteTextView
    ) {
        //Create a string array from enum of list types
        val types = enumValues<Type>()
        val typesAsString = arrayOfNulls<String>(types.count())
        for (i in types.indices) {
            typesAsString[i] = types[i].toString()
        }
        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_dropdown, typesAsString)
        dropDown.setAdapter(adapter)
        dropDown.setText(typesAsString[0], false)
    }
}