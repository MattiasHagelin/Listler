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
import com.math3249.listler.ui.viewmodel.AddItemViewModel
import com.math3249.listler.util.CATEGORY_ID
import com.math3249.listler.util.ITEM_ID
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.Utils
import com.math3249.listler.util.message.type.MessageType

class AddItemFragment: Fragment() {

    private val navArgs: AddItemFragmentArgs by navArgs()

    /*
    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }
    */

    private val viewModel: AddItemViewModel by activityViewModels {
        AddItemViewModel.AddItemViewModelFactory (
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
        val itemId = navArgs.itemId
        val categoryId = navArgs.categoryId

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
        if ( itemId > 0 ) {
            viewModel.getItem(itemId).observe(this.viewLifecycleOwner) {
                selectedItem -> item = selectedItem
                binding.apply {
                    itemInput.setText(item.name)
                    //categoryInput.setText(item.categoryId)
                }
            }
            binding.addItem.visibility = View.INVISIBLE
            binding.updateItem.visibility = View.VISIBLE
            binding.updateItem.setOnClickListener {
                val itemName = StringUtil.standardizeItemName(binding.itemInput.text.toString())
                val categoryName = StringUtil.standardizeItemName(binding.categoryDropdown.text.toString())

                if (checkUserInput(itemName, categoryName)) {
                    viewModel.updateItem(
                        navArgs.listId,
                        itemId,
                        itemName!!,
                        categoryId,
                        categoryName!!)
                    navigateBack()
                }
            }
        } else {
            binding.addItem.visibility = View.VISIBLE
            binding.itemInput.setText(navArgs.itemName)
        }

        viewModel.addItemFragmentMessage.observe(this.viewLifecycleOwner) {
            message ->
            if (!message.read) {
                when (message.type) {
                    else -> {
                        //MessageType.ITEM_INSERTED ->
                        viewModel.addItemToList(
                            navArgs.listId,
                            message.getId(CATEGORY_ID),
                            message.getId(ITEM_ID),
                            false
                        )
                        message.clear()
                        navigateBack()
                    }
                }
            }

        }
        binding.apply {
            itemInput.setText(navArgs.itemName)
            categoryDropdown.setText(navArgs.categoryName)
            addItem.setOnClickListener {
                if (checkUserInput(getItemName(), getCategoryName())) {
                    viewModel.addItemAndCategory(navArgs.listId, getItemName()!!, getCategoryName()!!)
                }
            }
        }
    }

    private fun navigateBack() {
        val action = AddItemFragmentDirections
            .actionAddItemFragmentToListDetailsFragment(navArgs.listId)
        findNavController().navigate(action)
    }

    private fun checkUserInput(itemName:String?, categoryName: String?): Boolean {

        if (!StringUtil.validateUserInput(itemName)) Utils.snackbar(MessageType.ITEM_INPUT_EMPTY, binding.root)
        if (!StringUtil.validateUserInput(categoryName)) Utils.snackbar(MessageType.CATEGORY_INPUT_EMPTY, binding.root)

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