package com.math3249.listler.ui.fragment.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.math3249.listler.R
import com.math3249.listler.databinding.DialogAddItemBinding
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.ui.fragment.navargs.AddItemArgs
import com.math3249.listler.util.KEY_INPUT
import com.math3249.listler.util.KEY_REQUEST
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.type.ListData

class AddItemDialog(
    private val itemArgs: AddItemArgs,
    val categories: List<String>
): DialogFragment() {

    private var _binding: DialogAddItemBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddItemBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
        binding.dialogLabel.hint = getString(R.string.hint_item_name)
        binding.dialogItem.setText(itemArgs.itemName)

        prepareCategoryDropdown()

        builder.setView(binding.root)
        if (itemArgs.itemName.isNotBlank() && itemArgs.catName.isNotBlank()) {
            builder.setTitle(getString(R.string.title_edit_item))
            .setPositiveButton(getString(R.string.btn_update)) { _, _ ->
                tryAddItem(Message.Type.ITEM_UPDATED)
            }
        } else {
            builder.setTitle(getString(R.string.title_new_item))
            .setPositiveButton(StringUtil.getString(R.string.btn_add)) { _, _->
                tryAddItem(Message.Type.ITEM_INSERTED)
            }
        }
        builder.setNegativeButton(StringUtil.getString(R.string.btn_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        return builder.create()
    }

    private fun prepareCategoryDropdown(){
        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_dropdown, categories)
        binding.apply {
            categoryDropdown.setAdapter(adapter)
            categoryDropdown.setText(categories.find { it == getString(R.string.misc) }, false)
            categoryDropdown.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    categoryDropdown.setText("")
            }

            categoryDropdown.setOnItemClickListener { _, _, _, _ ->
                val mapView = activity?.currentFocus
                 if (mapView != null) {
                     val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                     imm.hideSoftInputFromWindow(mapView.windowToken, 0)
                 }
            }
        }

    }

    private fun tryAddItem(type: Message.Type) {
        val itemName = StringUtil.standardizeItemName(
            binding.dialogItem.text.toString())
        val catName = StringUtil.standardizeItemName(
            binding.categoryDropdown.text.toString())
        if (itemName.isNotBlank() &&
            (catName.isNotBlank() && categories.contains(catName))) {
            setFragmentResult(
                KEY_REQUEST, bundleOf(
                    KEY_INPUT to ListMessage(
                        type,
                        true,
                        ListData(
                            listItem = ListCategoryItem(
                                itemArgs.listId,
                                catName,
                                itemName,
                                false
                            )))))
        } else {
            setFragmentResult(
                KEY_REQUEST, bundleOf(
                    KEY_INPUT to ListMessage(
                        Message.Type.INVALID_INPUT,
                        false
                    ))
            )
        }
    }

    companion object {
        const val TAG = "AddItemDialog"
    }
}