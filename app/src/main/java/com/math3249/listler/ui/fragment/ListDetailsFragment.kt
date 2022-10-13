package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListDetailsBinding
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItem
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.model.entity.Category
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.adapter.ListDetailAdapter
import com.math3249.listler.ui.fragment.dialog.AddItemDialog
import com.math3249.listler.ui.fragment.dialog.DeleteDialog
import com.math3249.listler.ui.fragment.navargs.AddItemArgs
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.listview.ListDetailCategory
import com.math3249.listler.ui.listview.ListDetailItem
import com.math3249.listler.ui.listview.RowType
import com.math3249.listler.ui.listview.RowTypes
import com.math3249.listler.ui.viewmodel.ListViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.utilinterface.Swipeable

class ListDetailsFragment(private val listDetailsArgs: ListDetailsArgs): Fragment(), Swipeable<RowType> {
    private val listId = listDetailsArgs.listId

    private val viewModel: ListViewModel by activityViewModels {
        ListViewModel.ListViewModelFactory (
            (activity?.application as App).database.listDao()
        )
    }

    private lateinit var items: List<Item>
    private  lateinit var selectedList: ListWithData

    private var _binding: FragmentListDetailsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ListDetailAdapter = createAdapter()
    private val adapter get() = _adapter

    private var _store: List<StoreCategoryCrossRef>? = null
    private val store get() = _store!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDetailsBinding.inflate(inflater, container, false)
        viewModel.getSortOrder(listId)
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
                addToList()
            }
            /*
            itemDropdown.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.action == KeyEvent.ACTION_UP
                ) {
                    addToDatabase()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }*/
        }
    }

    private fun subscribeToViewmodelMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            message as ListMessage
            when (message.type) {
                Type.ITEM_NEW -> {
                    if (selectedList.settings?.store?.store?.storeId == 1L) {

                    } else {
                        createAddItemDialog(AddItemArgs(
                            listId,
                            listDetailsArgs.listName,
                            message.listData.listItem.categoryName,
                            message.listData.listItem.itemName
                        ))
                    }

                }
               /*{
                    val action = ListDetailsTabFragmentDirections
                        .actionListDetailsTabFragmentToAddItemFragment(
                            AddItemArgs(
                                listId,
                                listDetailsArgs.listName,
                                itemName = message.listData.listItem.itemName
                            )
                        )
                    findNavController().navigate(action)
                }*/

                else -> Unit/*{
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
                }*/
            }
            message.clear()
        }
    }

    private fun subscribeToChildFragment() {
        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            val message = bundle.get(KEY_INPUT) as ListMessage
            if (message.success) {
                when (message.type) {
                    Type.ITEM_NEW -> addToDatabase(message.listData.listItem)
                    Type.ITEM_UPDATED -> addToList(message.listData.listItem)
                    Type.ITEM_DELETED -> viewModel.deleteItemFromList(message.listData.listItem)
                    Type.INVALID_INPUT -> Utils.snackbar(Type.INVALID_INPUT, binding.listRecyclerview)
                    else -> Unit
                }
            }
        }
    }

    private fun subscribeToList() {
        viewModel.getListWithCategoriesAndItems(listId)
            .observe(this.viewLifecycleOwner) { selectedList ->
                this.selectedList = selectedList
                selectedList.let { list ->
                    adapter.clearData()
                    adapter.insertData(sortList(list))
                   //adapter.submitList(listItems)
                    (requireActivity() as AppCompatActivity).supportActionBar?.title =
                        list.list.name
                }
            }
    }

    private fun sortList(
        list: ListWithData
    ): MutableList<RowType> {
        val misc = getString(R.string.misc)
        val listData: MutableList<RowType> = mutableListOf()
        val itemsByCat = list.listItems.groupBy { it.categoryName }
        val itemsMissing = itemsMissingCatInStore(
            list.settings?.store?.categories?.map { it.name }!!, itemsByCat)
        val sort = list.settings.store.storeCategories.sortedBy { it.sortOrder }
        val catByName = list.settings.store.categories.associateBy { it.categoryId }
        //Handle items missing category
        if (itemsByCat[misc] == null) {
            if (itemsMissing.isNotEmpty())
                listData.add(ListDetailCategory(ListCategoryItem(categoryName = misc)))
            listData.addAll(itemsMissing)
        }
        sort.forEach { storeCatSorted ->
            var first = true
            itemsByCat[catByName[storeCatSorted.categoryId]?.name]?.forEach { item ->
                if (!item.done) {
                    if (first) {
                        first = false
                        listData.add(ListDetailCategory(item))
                    }
                    //Handle items missing category
                    if (item.categoryName == misc)
                        listData.addAll(itemsMissing)
                    listData.add(ListDetailItem(item))
                }
            }
        }
        return listData
    }

    private fun itemsMissingCatInStore(
        storeCats: List<String>,
        itemsByCat: Map<String, List<ListCategoryItem>>
    ): Array<RowType> {
        val itemsMissing: MutableList<RowType> = mutableListOf()
        val itemsCategories = itemsByCat.keys.toList()
        itemsCategories.forEach { cat ->
            if (!storeCats.contains(cat))
                itemsByCat[cat]?.forEach { listItem ->
                    if (!listItem.done)
                        itemsMissing.add(ListDetailItem(listItem))
                }
        }
        return itemsMissing.toTypedArray()
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
    private fun addToList(listItem: ListCategoryItem? = null) {
        if (listItem != null) {
            viewModel.addItemToList(listItem)
        } else {
            val itemName =  StringUtil.standardizeItemName(binding.itemDropdown.text.toString())
            if (itemName.isNotBlank())
                viewModel.addItemToList(listId, itemName)
            else
                Utils.snackbar(Type.ITEM_NOT_IN_DATABASE, binding.root)
        }
        Utils.clearDropdown(binding.itemDropdown)
    }

    /**
     * Check if item exists in database
     * Navigate to Add Item fragment if it doesn't
     * Add item to list if it does
     */
    private fun addToDatabase(listItem: ListCategoryItem){
        //checks if itemDropdown has a value
        if (StringUtil.validateUserInput(listItem.itemName)) {
            if (!itemExists(listItem.itemName)) {
                //add new item to database
                viewModel.insert(listItem, true)
            } else {
                addToList()
            }
        }
    }

    private fun createAddItemDialog(itemArgs: AddItemArgs) {
        val categories = getCategories().map { it.name }
        AddItemDialog(itemArgs, categories).show(childFragmentManager, AddItemDialog.TAG)
    }

    private fun getItemNameFromItemDropdown(): String {
        val input = StringUtil.standardizeItemName(binding.itemDropdown.text.toString())
        return if (!StringUtil.validateUserInput(input)) {
            Utils.snackbar(Type.ITEM_INPUT_EMPTY, binding.listRecyclerview)
            ""
        } else input
    }

    private fun itemExists(itemName: String): Boolean {
        return items.isNotEmpty()
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
            createAddItemDialog(AddItemArgs(
                listDetailsArgs.listId,
                listDetailsArgs.listName,
                item.listItem.categoryName,
                item.listItem.itemName
            ))
        }
    }

    private fun swipe(){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(this))
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerview)
    }

    private fun getCategories(): List<Category> {
        val storeId = selectedList.settings?.listSettings?.storeId
        return if (storeId != null && storeId > 1)
            selectedList.settings?.store?.categories!!
        else
            viewModel.allCategories!!
    }
}