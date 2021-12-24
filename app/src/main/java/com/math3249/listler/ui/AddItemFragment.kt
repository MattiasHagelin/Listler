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
        /*
        TODO: Check if this code is still needed
        viewModel.message.observe(this.viewLifecycleOwner) {
            message -> message.let {
                if (message != null && !message.success) {
                    Utils.snackbar(message.type, binding.rootLayout)
                    viewModel.message.value = null
                }
            }
        }
         */
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
                viewModel.updateItem(id,
                    binding.itemInput.text.toString(),
                    0L) //TODO:Get categories from table
                navigateBack()
            }
        } else {
            binding.addItem.visibility = View.VISIBLE
            binding.itemInput.setText(navArgs.itemName)
        }

        viewModel.insertId.observe(this.viewLifecycleOwner) {
            id -> id.let {
                if (id != null) {
                    viewModel.addItemToList(id, navArgs.listId, false)
                    viewModel.insertId.value = null
                    navigateBack()
                }
            }
        }
        binding.apply {

            itemInput.setText(navArgs.itemName)
           // categoryInput.setText("default")
            //categoryInput.requestFocus()

            addItem.setOnClickListener {
                val itemName = Utils.standardizeItemName(itemInput.text.toString())
                val categoryName = 0L //TODO: Get category id
                if (!itemName.isNullOrBlank()) {
                        viewModel.addItem(itemName, categoryName)
                }
            }

        }
    }

    private fun navigateBack() {
        val action = AddItemFragmentDirections
            .actionAddItemFragmentToListDetailsFragment(navArgs.listId, "default")
        findNavController().navigate(action)
    }
}