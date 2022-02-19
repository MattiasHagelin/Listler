package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentAddItemBinding
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.viewmodel.ItemViewModel
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.Utils
import com.math3249.listler.util.message.Message.Type

class AddItemFragment: Fragment() {

    private val navArgs: AddItemFragmentArgs by navArgs()

    /*
    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }
    */

    private val viewModel: ItemViewModel by activityViewModels {
        ItemViewModel.AddItemViewModelFactory (
            (activity?.application as App).database.itemDao()
        )
    }
    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var item: Item

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding
            .inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryName = navArgs.addItemData.catName
        val itemName = navArgs.addItemData.itemName

        viewModel.allCategories.observe(this.viewLifecycleOwner) {
            categories ->
            categories.let {
                val arr = mutableListOf<String>()
                for (value in it){
                    arr.add(value.name)
                }
                val dropDownAdapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.add_list_dropdown, arr)
                binding.categoryDropdown.setAdapter(dropDownAdapter)
            }
        }
        if ( itemName.isNotBlank() && categoryName.isNotBlank()) {
            binding.apply {
                itemInput.setText(itemName)
                categoryDropdown.setText(navArgs.addItemData.catName)
                }

            binding.actionBar.saveButton.text = getString(R.string.btn_update)
            binding.actionBar.saveButton.setOnClickListener {
                val itemInput = StringUtil.standardizeItemName(binding.itemInput.text.toString())
                val catInput = StringUtil.standardizeItemName(binding.categoryDropdown.text.toString())

                if (checkUserInput(itemInput, catInput)) {
                    viewModel.updateItemOnList(
                        navArgs.addItemData,
                    catInput!!,
                    itemInput!!)
                    navigateBack()
                }
            }
        } else {
            binding.apply {
                actionBar.saveButton.text = getString(R.string.btn_save)
                itemInput.setText(navArgs.addItemData.itemName)
                categoryDropdown.setText(getText(R.string.misc))

                actionBar.saveButton.setOnClickListener {
                    if (checkUserInput(getItemName(), getCategoryName())) {
                        viewModel.addItemAndCategory(navArgs.addItemData.listId, getItemName()!!, getCategoryName()!!)
                    }
                }
            }
        }
        subscribeToMessage()

        binding.apply {
            categoryDropdown.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    categoryDropdown.setText("")
                else if (categoryDropdown.text.toString() == "") {
                    if (navArgs.addItemData.catName == "")
                        categoryDropdown.setText(getText(R.string.misc))
                    else
                        categoryDropdown.setText(navArgs.addItemData.catName)
                }

            }
     //       itemInput.setText(navArgs.addItemData.itemName)
     //       categoryDropdown.setText(navArgs.addItemData.catName)
            actionBar.cancelButton.setOnClickListener {
                findNavController().navigateUp()
            }

        }
    }

    private fun subscribeToMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) {
                message ->
                when (message.type) {
                    Type.ITEM_IN_LIST -> {
                        Utils.snackbar(Type.ITEM_IN_LIST, binding.root, message.listData.listItem.itemName)
                    }
                    Type.NO_MESSAGE -> {}
                    else -> {
                        navigateBack()
                    }
                }
            message.clear()
        }
    }

    private fun navigateBack() {
        val action = AddItemFragmentDirections
            .actionAddItemFragmentToListDetailsTabFragment(
                ListDetailsArgs(navArgs.addItemData.listId, navArgs.addItemData.listName)
            )
        findNavController().navigate(action)
    }

    private fun checkUserInput(itemName:String?, categoryName: String?): Boolean {

        if (!StringUtil.validateUserInput(itemName)) Utils.snackbar(Type.ITEM_INPUT_EMPTY, binding.root)
        if (!StringUtil.validateUserInput(categoryName)) Utils.snackbar(Type.CATEGORY_INPUT_EMPTY, binding.root)

        return StringUtil.validateUserInput(itemName)
                && StringUtil.validateUserInput(categoryName)
    }

    private fun getItemName(): String? {
        return StringUtil.standardizeItemName(binding.itemInput.text.toString())
    }

    private fun getCategoryName(): String? {
        return StringUtil.standardizeItemName(binding.categoryDropdown.text.toString())
    }

}