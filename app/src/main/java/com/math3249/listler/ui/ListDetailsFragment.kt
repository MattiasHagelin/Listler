package com.math3249.listler.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListDetailsBinding
import com.math3249.listler.ui.adapter.ListDetailCategoryAdapter
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.App
import com.math3249.listler.model.entity.Item
import com.math3249.listler.util.message.Type.MessageType
import com.math3249.listler.util.Utils

class ListDetailsFragment: Fragment() {

    private val navArgs: ListDetailsFragmentArgs by navArgs()

    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private lateinit var items: List<Item>
    private var selectedItem: Item? = null

    private var _binding: FragmentListDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navArgs.id
        val adapter = ListDetailCategoryAdapter (
            {
                //item -> viewModel.updateItemOnList(id, item.itemId, true)
                //TODO: Implement clickLiserner for Category header
        }, {/*
            item ->
                val action = ListDetailsFragmentDirections
                    .actionListDetailsFragmentToAddItemFragment("", id, item.itemId)
            findNavController().navigate(action)
            TODO: Implement longClickLisener for Category header
            */
        })

        viewModel.allItems.observe(this.viewLifecycleOwner) {
            allItems ->
            items = allItems
            allItems.let {
                val arr = mutableListOf<String>()
                for (value in it){
                    arr.add(value.name)
                }
                val dropDownAdapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.add_list_dropdown, arr)
                binding.itemDropdown.setAdapter(dropDownAdapter)
            }
        }

        viewModel.getItems(id).observe(this.viewLifecycleOwner) {
            selectedList ->
            selectedList.let {
                adapter.submitList(it.items)
                (requireActivity() as AppCompatActivity).supportActionBar?.title = it.list.name
            }
        }

        viewModel.message.observe(this.viewLifecycleOwner) {
            message -> message.let {
                var isSent = false
                if (message != null) {
                    if (message.itemId > 0) {
                        val item: Item? = items.find { it.itemId == message.itemId }
                        if (item != null) {
                            Utils.snackbar(message.type, binding.categoryRecyclerview, item.name)
                            isSent = true
                        }
                    }
                    if (!isSent) {
                        Utils.snackbar(message.type, binding.categoryRecyclerview)
                    }
                    viewModel.message.value = null
                }
            }
        }

        binding.apply {
            categoryRecyclerview.adapter = adapter

            itemDropdown.setOnClickListener {
                addToDatabase()
            }
            itemDropdown.setOnItemClickListener { _, _, _, _ ->
                addToList()
            }
            itemDropdown.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_UP) {
                    addToDatabase()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }
    /** Adds item to current list and
    clears text from item_dropdown
     */
    private fun addToList() {
        selectedItem = items.find { it.name == getItemNameFromItemDropdown() }

        if (selectedItem != null) {
          if (selectedItem!!.itemId > 0) {
              viewModel.addItemToList(selectedItem!!.itemId, navArgs.id, false)
              clearItemDropdown()
          }
        } else
            Utils.snackbar(MessageType.ITEM_NOT_IN_DATABASE, binding.categoryRecyclerview)
    }

    /**
     * Check if item exists in database
     * Navigate to Add Item fragment if it doesn't
     * Add item to list if it does
     */
    private fun addToDatabase(){
        //checks if itemDropdown has a value
        if (getItemNameFromItemDropdown() != "") {
            viewModel.itemExists(getItemNameFromItemDropdown()).observe(this.viewLifecycleOwner) {
                if (!it) {
                    //add new item to database
                    //TODO: navigate to fragment_add_item
                    val action = ListDetailsFragmentDirections
                        .actionListDetailsFragmentToAddItemFragment(
                            getItemNameFromItemDropdown(),
                            navArgs.id,
                            0
                        )
                    findNavController().navigate(action)
                } else {
                    addToList()
                }
            }
        }
    }
    private fun clearItemDropdown() {
        binding.itemDropdown.setText("", false)
    }
    private fun getItemNameFromItemDropdown(): String {
        return Utils.standardizeItemName(binding.itemDropdown.text.toString())
    }

    private fun bindList() {
        binding.apply {
            //name.text = list.list.name
        }
    }
}