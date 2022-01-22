package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentCompletedDetailsBinding
import com.math3249.listler.model.ListWithData
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.ui.adapter.ListDetailAdapter
import com.math3249.listler.ui.listview.*
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.util.DragSwipe
import com.math3249.listler.util.LEFT
import com.math3249.listler.util.Settings
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.utilinterface.Swipeable

class CompletedDetailsFragment(val listId: Long): Fragment(), Swipeable<RowType> {

    //TODO:private val navArgs: ListDetailsFragmentArgs by navArgs()
    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private  lateinit var selectedList: ListWithData
    //private var selectedItem: Item? = null

    private var _binding: FragmentCompletedDetailsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ListDetailAdapter? = null
    private val adapter get() = _adapter!!

    private lateinit var _settings: Settings
    private val settings get() = _settings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompletedDetailsBinding.inflate(inflater, container, false)
        _settings = Settings(requireContext())
        _adapter = ListDetailAdapter ({ item ->
            if (item.getRowType() == RowTypes.ITEM.ordinal) {
                viewModel.updateItemOnList(listId, item.id, false)
            }
        },
            { item ->
            })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO:val listId = navArgs.listId

        viewModel.getListWithCategoriesAndItems(listId).observe(this.viewLifecycleOwner) {
            selectedList ->

            this.selectedList = selectedList
            selectedList.let { list ->
                val itemsById = list.items.associateBy { it.itemId } //list.listItems.filter {it.listId == list.list.listId}.associateBy { it.itemId }
                val crossRefByCategory = list.listItems.groupBy { it.categoryId }
                val listData  = mutableListOf<RowType>()
                list.categories.forEach { category ->
                    var first = true
                    crossRefByCategory[category.categoryId]!!.forEach { crossRefItem ->
                        val tempItem = itemsById[crossRefItem.itemId]
                        if (tempItem != null) {
                            if (crossRefItem.done && showItem(crossRefItem.modifiedAt)) {
                                if (first) {
                                    listData.add(ListDetailCategory(category.name, category.categoryId))
                                    first = false
                                }
                                listData.add(ListDetailItem(
                                    category.categoryId,
                                    category.name,
                                    tempItem.name,
                                    tempItem.itemId
                                ))
                            }
                        }
                    }
                }
                adapter.submitList(listData)
                (requireActivity() as AppCompatActivity).supportActionBar?.title = list.list.name
            }
        }

        viewModel.listDetailFragmentMessage.observe(this.viewLifecycleOwner) {
            message ->
            if (message?.type != null) {
                /*
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
                */
            }
        }

        swipe()

        binding.apply {
            listRecyclerview.adapter = adapter
        }
    }

    private fun swipe(){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(this))
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerview)
    }

    private fun showItem(modifiedTime: Long): Boolean {
        val elapsedTimeInHours = ((System.nanoTime() - modifiedTime)/1_000_000_000.0/3600.0)
        val timeLimit = settings.getInt(StringUtil.getString(R.string.key_completed_item_time_limit), 1)
        return elapsedTimeInHours <= timeLimit
    }

    override val swipeDirs: Int
        get() = LEFT
    override val parentContext: Context
        get() = requireContext()
    override val listAdapter: ListAdapter<RowType, RecyclerView.ViewHolder>
        get() = adapter

    override fun swipeLeft(position: Int) {
        val item = adapter.getRowType(position)
        if (item.getRowType() == RowTypes.ITEM.ordinal) {
            viewModel.deleteItemFromList(
                ListCategoryItemCrossRef(
                    listId = listId,
                    itemId = item.getData()[RowTypeKey.ITEM_ID]?.toLongOrNull() ?: 0,
                    categoryId = item.getData()[RowTypeKey.CATEGORY_ID]?.toLongOrNull() ?: 0
                )
            )
        }
    }

    override fun swipeRight(position: Int) {
        return
    }
}