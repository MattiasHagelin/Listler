package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListDetailsBinding
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.adapter.ListDetailAdapter
import com.math3249.listler.ui.listview.*
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.type.MessageType

class ListDetailsFragment: Fragment() {
    private val navArgs: ListDetailsFragmentArgs by navArgs()

    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private lateinit var items: List<Item>
    private  lateinit var selectedList: ListWithCategoriesAndItems

    private var _binding: FragmentListDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        MenuUtil().prepareMenu(menu, LIST_DETAIL_FRAGMENT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_details_to_completed_items -> {
                val action = ListDetailsFragmentDirections
                    .actionListDetailsFragmentToCompletedDetailsFragment(navArgs.listId)
                findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listId = navArgs.listId
        val adapter = createAdapter()


        subscribeToItems()
        subscribeToList(adapter)
        subscribeToViewmodelMessage()

        swipe(listId, viewModel, adapter)

        bind(adapter)
    }

    private fun bind(adapter: ListDetailAdapter) {
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
            if (!message!!.read) {
                when (message.type) {
                    MessageType.ITEM_MISSING_CATEGORY -> {
                        val action = ListDetailsFragmentDirections
                            .actionListDetailsFragmentToAddItemFragment(
                                message.getId(LIST_ID),
                                message.getId(ITEM_ID),
                                message.extra,
                                -1,
                                ""
                            )
                        message.clear()
                        findNavController().navigate(action)
                    }
                    MessageType.READ_MESSAGE -> {}
                    else -> {
                        val action = ListDetailsFragmentDirections
                            .actionListDetailsFragmentToAddItemFragment(
                                message.getId(LIST_ID),
                                message.getId(ITEM_ID),
                                message.extra,
                                -1,
                                ""
                            )
                        message.clear()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun subscribeToList(
        adapter: ListDetailAdapter
    ) {
        viewModel.getListWithCategoriesAndItems(navArgs.listId)
            .observe(this.viewLifecycleOwner) { selectedList ->
                this.selectedList = selectedList
                selectedList.let { list ->
                    val itemsById =
                        list.items.associateBy { it.itemId } //list.listItems.filter {it.listId == list.list.listId}.associateBy { it.itemId }
                    val crossRefByCategory = list.listItems.groupBy { it.categoryId }
                    val listData = mutableListOf<RowType>()
                    list.categories.forEach { category ->
                        var first = true
                        crossRefByCategory[category.categoryId]!!.forEach { crossRefItem ->
                            val tempItem = itemsById[crossRefItem.itemId]
                            if (tempItem != null) {
                                if (!crossRefItem.done) {
                                    if (first) {
                                        listData.add(
                                            ListDetailCategory(
                                                category.name,
                                                category.categoryId
                                            )
                                        )
                                        first = false
                                    }
                                    listData.add(
                                        ListDetailItem(
                                            category.categoryId,
                                            category.name,
                                            tempItem.name,
                                            tempItem.itemId
                                        )
                                    )
                                }
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
            viewModel.updateItemOnList(navArgs.listId, item.id, true)
        }
    },
        { item ->
            if (item.getRowType() == RowTypes.ITEM.ordinal) {
                val action = ListDetailsFragmentDirections
                    .actionListDetailsFragmentToAddItemFragment(
                        navArgs.listId,
                        item.getData()[RowTypeKey.ITEM_ID]?.toLongOrNull() ?: -1,
                        item.getData()[RowTypeKey.ITEM] ?: "",
                        item.getData()[RowTypeKey.CATEGORY_ID]?.toLongOrNull() ?: -1,
                        item.getData()[RowTypeKey.CATEGORY] ?: ""
                    )
                findNavController().navigate(action)
            }// else {
                /*
        val adapter = ListDetailCategoryAdapter (
            {
                //item -> viewModel.updateItemOnList(id, item.itemId, true)
                //TODO: Implement clickListener for Category header
        }, {/*
            item ->
                val action = ListDetailsFragmentDirections
                    .actionListDetailsFragmentToAddItemFragment("", id, item.itemId)
            findNavController().navigate(action)
            TODO: Implement longClickListener for Category header
            */
        })*/
           // }
        })

    /** Adds item to current list and
    clears text from item_dropdown
     */
    private fun addToList() {
        val selectedItem = items.find { it.name == getItemNameFromItemDropdown() }

        if (selectedItem != null) {
          if (selectedItem.itemId > 0) {
              viewModel.addItemToList(navArgs.listId, selectedItem)
              Utils.clearItemDropdown(binding.itemDropdown)
          }
        } else
            Utils.snackbar(MessageType.ITEM_NOT_IN_DATABASE, binding.listRecyclerview)
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
                    val action = ListDetailsFragmentDirections
                        .actionListDetailsFragmentToAddItemFragment(navArgs.listId
                            , 0
                            , getItemNameFromItemDropdown()
                            , 0
                            , "")
                    findNavController().navigate(action)
                } else {
                    addToList()
                }
        }
    }

    private fun getItemNameFromItemDropdown(): String {
        val input = StringUtil.standardizeItemName(binding.itemDropdown.text.toString())
        return if (!StringUtil.validateUserInput(input)) {
            Utils.snackbar(MessageType.ITEM_INPUT_EMPTY, binding.listRecyclerview)
            ""
        } else input!!
    }

    private fun itemExists(): Boolean {
        return items.count { it.name == getItemNameFromItemDropdown() } > 0
    }

    private fun swipe(listId: Long, viewModel: ListDetailViewModel, adapter: ListDetailAdapter){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(
            swipeDirs = ItemTouchHelper.LEFT,
            icon = AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_delete_24),
            swipeLeft = { position ->
                val item = adapter.getRowType(position)
                if (item.getRowType() == RowTypes.ITEM.ordinal) {
                    viewModel.deleteItemFromList(
                        ListCategoryItemCrossRef(
                            listId = listId,
                            itemId = item.getData()[RowTypeKey.ITEM_ID]?.toLongOrNull() ?: 0,
                            categoryId = item.getData()[RowTypeKey.CATEGORY_ID]?.toLongOrNull() ?: 0
                        ))
                }
            }))
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerview)
    }
}