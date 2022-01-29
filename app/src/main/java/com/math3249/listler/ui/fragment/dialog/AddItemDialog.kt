package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.math3249.listler.R
import com.math3249.listler.databinding.DialogAddItemBinding
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message

class AddItemDialog(
    val itemId: Long,
    val itemName: String,
    val categories: List<String>
): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogAddItemBinding.inflate(
            LayoutInflater.from(context)
        )
        val builder = AlertDialog.Builder(context)
        binding.dialogLabel.hint = getString(R.string.hint_item_name)
        binding.dialogItem.setText(itemName)

        prepareCategoryDropdown(binding)

        builder.setView(binding.root)
        if (itemId <= 0) {
            builder.setTitle(getString(R.string.title_new_item))
            .setPositiveButton(StringUtil.getString(R.string.btn_add)) { _, _->
                val input = StringUtil.standardizeItemName(
                    binding.dialogItem.text.toString())
                val category = StringUtil.standardizeItemName(binding.categoryDropdown.text.toString())
                if (input == null || category == null)
                    setFragmentResult(
                        KEY_REQUEST, bundleOf(
                            KEY_INPUT to Message(
                                Message.Type.INVALID_INPUT,
                                false
                        ))
                    )
                else
                    setFragmentResult(
                        KEY_REQUEST, bundleOf(
                            KEY_INPUT to Message(
                                Message.Type.ITEM_INSERTED,
                                true
                            )
                        )
                    )
            }} else {
                builder.setTitle(getString(R.string.btn_update))
            }

        builder.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    private fun prepareCategoryDropdown(binding: DialogAddItemBinding){
        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_dropdown, categories)
        binding.categoryDropdown.setAdapter(adapter)
        binding.categoryDropdown.setText(categories[0], false)
    }
}