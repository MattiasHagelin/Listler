package com.math3249.listler.ui

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
import com.math3249.listler.util.Utils
import com.math3249.listler.util.message.Type.MessageType

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
    ): View? {
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
        val id = navArgs.itemId

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
        if ( id > 0 ) {
            viewModel.getItem(id).observe(this.viewLifecycleOwner) {
                selectedItem -> item = selectedItem
                binding.apply {
                    itemInput.setText(item.name)
                    //categoryInput.setText(item.categoryId)
                }
            }
            binding.addItem.visibility = View.INVISIBLE
            binding.updateItem.visibility = View.VISIBLE
            binding.updateItem.setOnClickListener {
                val itemName = Utils.standardizeItemName(binding.itemInput.text.toString())
                val categoryName = Utils.standardizeItemName(binding.categoryDropdown.text.toString())

                if (checkUserInput(itemName, categoryName)) {
                    viewModel.updateItem(id
                        , itemName!!
                        , categoryName!!)
                    navigateBack()
                }
            }
        } else {
            binding.addItem.visibility = View.VISIBLE
            binding.itemInput.setText(navArgs.itemName)
        }

        viewModel.newItemId.observe(this.viewLifecycleOwner) {
            id -> id.let {
                if (id != null) {
                    viewModel.addItemToList(id, navArgs.listId, false)
                    viewModel.newItemId.value = null
                    navigateBack()
                }
            }
        }
        binding.apply {
            itemInput.setText(navArgs.itemName)
            categoryDropdown.setText(navArgs.categoryName)
            categoryLabel.setEndIconOnClickListener {
                clickEndIcon()
            }
            addItem.setOnClickListener {
                if (checkUserInput(getItemName(), getCategoryName())) {
                    viewModel.addItem(getItemName()!!, getCategoryName()!!)
                }
            }
        }
    }

    private fun navigateBack() {
        val action = AddItemFragmentDirections
            .actionAddItemFragmentToListDetailsFragment(navArgs.listId)
        findNavController().navigate(action)
    }

    private fun clickEndIcon() {

        if (checkUserInput(getItemName(), getCategoryName())){
            addCategoryToDatabase()
        }
    }

    private fun checkUserInput(itemName:String?, categoryName: String?): Boolean {
        if (itemName == null) Utils.snackbar(MessageType.ITEM_INPUT_EMPTY, binding.rootLayout)
        if (categoryName == null) Utils.snackbar(MessageType.CATEGORY_INPUT_EMPTY, binding.rootLayout)

        return itemName != null && categoryName != null
    }

    private fun getItemName(): String? {
        return Utils.standardizeItemName(binding.itemInput.text.toString())
    }

    private fun getCategoryName(): String? {
        return Utils.standardizeItemName(binding.categoryDropdown.text.toString())
    }

    private fun addCategoryToDatabase() {
            viewModel.addCategory(getCategoryName()!!)
            viewModel.newCategoryId.observe(this.viewLifecycleOwner) {
            if (it != null && it < 0) {
                Utils.snackbar(MessageType.CATEGORY_IN_DATABASE, binding.rootLayout, getCategoryName()!!)
            }
        }

    }
}