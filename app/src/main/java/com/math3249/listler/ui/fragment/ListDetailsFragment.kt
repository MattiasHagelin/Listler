package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListDetailsBinding
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.adapter.ListDetailAdapter
import com.math3249.listler.ui.fragment.dialog.DeleteDialog
import com.math3249.listler.ui.fragment.navargs.AddItemArgs
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.listview.*
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.utilinterface.Swipeable

class ListDetailsFragment(private val listDetailsArgs: ListDetailsArgs): Fragment(), Swipeable<RowType> {
    private val listId = listDetailsArgs.listId

    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private lateinit var items: List<Item>
    private  lateinit var selectedList: ListWithData

    private var _binding: FragmentListDetailsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ListDetailAdapter = createAdapter()
    private val adapter get() = _adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //val adapter = createAdapter()

        subscribeToItems()
        subscribeToList()
        subscribeToViewmodelMessage()
        subscribeToChildFragment()


        swipe()
        bind()
    }

    private fun bind() {
        binding.apply {

            listRecyclerview.adapter = adapter
            itemDropdown.requestFocus()
            itemDropdown.setOnItemClickListener { _, _, _, _ ->
                addToList()
            }

            addItemToList.setEndIconOnClickListener {
                addToDatabase()
            }
            itemDropdown.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_UP
                ) {
                    addToDatabase()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun subscribeToViewmodelMessage() {
        viewModel.listDetailFragmentMessage.observe(this.viewLifecycleOwner) { message ->
            when (message.type) {
                Type.ITEM_MISSING_CATEGORY -> {
                    message as ListMessage
                    val action = ListDetailsTabFragmentDirections
                        .actionListDetailsTabFragmentToAddItemFragment(
                            AddItemArgs(
                                listId,
                                listDetailsArgs.listName,
                                itemName = message.listData.listItem.itemName
                            )
                        )
                    findNavController().navigate(action)
                }
                Type.NO_MESSAGE -> {}
                else -> {
                    message as ListMessage
                    val action = ListDetailsTabFragmentDirections
                        .actionListDetailsTabFragmentToAddItemFragment(
                            AddItemArgs(
                                listId,
                                listDetailsArgs.listName,
                                itemName = message.listData.listItem.itemName
                            )
                        )
                    findNavController().navigate(action)
                }
            }
            message.clear()
        }
    }

    private fun subscribeToChildFragment() {
        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            val message = bundle.get(KEY_INPUT) as ListMessage
            if (message.success) {
                viewModel.deleteItemFromList(
                    message.listData.listItem
                )
            }
        }
    }

    private fun subscribeToList() {
        viewModel.getListWithCategoriesAndItems(listId)
            .observe(this.viewLifecycleOwner) { selectedList ->
                this.selectedList = selectedList
                selectedList.let { list ->
                    val itemsByCategory = list.listItems.groupBy { it.categoryName }
                    val listData = mutableListOf<RowType>()
                    for ((k, v) in itemsByCategory) {
                        var first = true
                        v.forEach { item ->
                            if (!item.done) {
                                if (first) {
                                    first = false
                                    listData.add(
                                        ListDetailCategory(
                                            v.first()
                                        )
                                    )
                                }
                                listData.add(ListDetailItem(
                                    item
                                ))
                            }
                        }
                    }
                    adapter.submitList(listData)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        list.list.name
                }
            }
    }

    private fun subscribeToItems() {
        viewModel.allItems.observe(this.viewLifecycleOwner) { allItems ->
            items = allItems
            allItems.let {
                val arr = mutableListOf<String>()
                for (value in it) {
                    arr.add(value.name)
                }
                val dropDownAdapter: ArrayAdapter<String> =
                    ArrayAdapter(requireContext(), R.layout.add_list_dropdown, arr)

                binding.itemDropdown.setAdapter(dropDownAdapter)
            }
        }
    }

    private fun createAdapter() = ListDetailAdapter({ item ->
            if (item.getRowType() == RowTypes.ITEM.ordinal) {
                viewModel.updateItemOnList(item.listItem, true)
            }
        })

    /** Adds item to current list and
    clears text from item_dropdown
     */
    private fun addToList() {
        val selectedItem = items.find { it.name == getItemNameFromItemDropdown() }

        if (selectedItem != null) {
          if (selectedItem.itemId > 0) {
              viewModel.addItemToList(listId, selectedItem.name)
              Utils.clearDropdown(binding.itemDropdown)
          }
        } else
            Utils.snackbar(Type.ITEM_NOT_IN_DATABASE, binding.listRecyclerview)
    }

    /**
     * Check if item exists in database
     * Navigate to Add Item fragment if it doesn't
     * Add item to list if it does
     */
    private fun addToDatabase(){
        //checks if itemDropdown has a value
        if (StringUtil.validateUserInput(getItemNameFromItemDropdown())) {
                if (!itemExists()) {
                    //add new item to database
                    val action = ListDetailsTabFragmentDirections
                        .actionListDetailsTabFragmentToAddItemFragment(
                            AddItemArgs(
                                listId,
                                listDetailsArgs.listName,
                                0,
                                "",
                                0,
                                getItemNameFromItemDropdown()
                            )
                        )
                    findNavController().navigate(action)
                } else {
                    addToList()
                }
        }
    }

    private fun getItemNameFromItemDropdown(): String {
        val input = StringUtil.standardizeItemName(binding.itemDropdown.text.toString())
        return if (!StringUtil.validateUserInput(input)) {
            Utils.snackbar(Type.ITEM_INPUT_EMPTY, binding.listRecyclerview)
            ""
        } else input!!
    }

    private fun itemExists(): Boolean {
        return items.count { it.name == getItemNameFromItemDropdown() } > 0
    }

    override val swipeDirs: Int
        get() = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

    override val parentContext: Context
        get() = requireContext()

    override val listAdapter: ListAdapter<RowType, RecyclerView.ViewHolder>
        get() = adapter

    override fun swipeLeft(position: Int) {
        val item = adapter.getRowType(position)
        if (item.getRowType() == RowTypes.ITEM.ordinal) {
            DeleteDialog(item.listItem,
                position
                ).show(childFragmentManager, DeleteDialog.TAG)
        }
    }

    override fun swipeRight(position: Int) {
        val item = adapter.getRowType(position)
        if (item.getRowType() == RowTypes.ITEM.ordinal) {
            val action = ListDetailsTabFragmentDirections
                .actionListDetailsTabFragmentToAddItemFragment(
                    AddItemArgs(
                        listDetailsArgs.listId,
                        listDetailsArgs.listName,
                        item.listItem.categoryId,
                        item.listItem.categoryName,
                        item.listItem.itemId,
                        item.listItem.itemName
                    ))
            findNavController().navigate(action)
        }
    }



    private fun swipe(){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(this))
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerview)
    }
}