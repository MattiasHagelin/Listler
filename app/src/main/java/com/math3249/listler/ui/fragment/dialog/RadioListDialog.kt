package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.math3249.listler.R
import com.math3249.listler.databinding.DialogRadioListBinding
import com.math3249.listler.util.KEY_ENTRY_TYPE
import com.math3249.listler.util.KEY_ENTRY_VALUE
import com.math3249.listler.util.KEY_REQUEST
import com.math3249.listler.util.StringUtil

class RadioListDialog(
    private val entries: Array<String>,
    private val entryValues: Array<String>,
    private val selectedValue: Int,
    private val type: Int
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogRadioListBinding.inflate(
            layoutInflater
        )
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)

        for (i in entries.indices) {
            val radioBtn = RadioButton(context)
            if (entryValues[i].toInt() == selectedValue)
                radioBtn.isChecked = true
            radioBtn.text = entries[i]
            radioBtn.id = entryValues[i].toInt()
            radioBtn.setOnClickListener { _ ->
                setFragmentResult(
                    KEY_REQUEST, bundleOf(
                        KEY_ENTRY_VALUE to radioBtn.id,
                        KEY_ENTRY_TYPE to type
                    )
                )
                dismiss()
            }
            binding.radioGroupList.addView(radioBtn)
        }

        builder.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
            dialog.cancel()
        }
        return builder.create()
    }

    companion object {
        const val TAG = "RadioListDialog"
    }
}