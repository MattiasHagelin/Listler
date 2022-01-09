package com.math3249.listler.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.math3249.listler.R
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.App
import com.math3249.listler.databinding.FragmentCompletedDetailsBinding
import com.math3249.listler.model.ListWithCategoriesAndItems
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.adapter.ListDetailAdapter
import com.math3249.listler.ui.listview.*
import com.math3249.listler.util.Settings
import com.math3249.listler.util.StringUtil
import com.math3249.listler.util.Swipe
import com.math3249.listler.util.message.Type.MessageType

class CompletedDetailsFragment: Fragment() {

    private val navArgs: ListDetailsFragmentArgs by navArgs()
    private val viewModel: ListDetailViewModel by activityViewModels {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private lateinit var items: List<Item>
    private  lateinit var selectedList: ListWithCategoriesAndItems
    //private var selectedItem: Item? = null

    private var _binding: FragmentCompletedDetailsBinding? = null
    private val binding get() = _binding!!


    private lateinit var _settings: Settings
    private val settings get() = _settings

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompletedDetailsBinding.inflate(inflater, container, false)
        _settings = Settings(requireContext())
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_completed_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.completed_items_to_list_details) {
            val action = CompletedDetailsFragmentDirections
                .actionCompletedDetailsFragmentToListDetailsFragment(navArgs.listId)
            findNavController().navigate(action)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listId = navArgs.listId
        val adapter = ListDetailAdapter ({ item ->
            if (item.getRowType() == RowTypes.ITEM.ordinal) {
                viewModel.updateItemOnList(listId, item.id, false)
            }
        },
            { item ->
                if (item.getRowType() == RowTypes.ITEM.ordinal) {
                    val action = ListDetailsFragmentDirections
                        .actionListDetailsFragmentToAddItemFragment(
                            listId,
                            item.getData()[RowTypeKey.ITEM_ID]?.toLongOrNull() ?: -1,
                            item.getData()[RowTypeKey.ITEM] ?: "",
                            item.getData()[RowTypeKey.CATEGORY_ID]?.toLongOrNull() ?: -1,
                            item.getData()[RowTypeKey.CATEGORY] ?: ""
                        )
                    findNavController().navigate(action)
                } else {
                    //val action = ListDetailsFragmentDirections
                      //  .
                }
            })
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
            if (!message!!.messageRead) {
                when (message.type) {
                    MessageType.ITEM_MISSING_CATEGORY -> {
                        val action = ListDetailsFragmentDirections
                            .actionListDetailsFragmentToAddItemFragment(
                                message.listId,
                                message.itemId,
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
                                message.listId,
                                message.itemId,
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

        swipe(listId, viewModel, adapter)

        binding.apply {
            listRecyclerview.adapter = adapter
        }
    }

    private fun swipe(listId: Long, viewModel: ListDetailViewModel, adapter: ListDetailAdapter){
        val itemTouchHelper = ItemTouchHelper(Swipe(
            AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_delete_24)
        ) { position ->
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
        })
        itemTouchHelper.attachToRecyclerView(binding.listRecyclerview)
    }

    private fun showItem(modifiedTime: Long): Boolean {
        val elapsedTimeInHours = ((System.nanoTime() - modifiedTime)/1_000_000_000.0/3600.0)
        val timeLimit = settings.getInt(StringUtil.getString(R.string.key_completed_item_time_limit), 1)
        return elapsedTimeInHours <= timeLimit
    }
}