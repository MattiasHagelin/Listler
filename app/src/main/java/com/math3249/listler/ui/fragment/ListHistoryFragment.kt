package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListHistoryBinding
import com.math3249.listler.model.crossref.ListCategoryItemCrossRef
import com.math3249.listler.model.entity.Item
import com.math3249.listler.ui.adapter.ListHistoryAdapter
import com.math3249.listler.ui.fragment.dialog.DeleteDialog
import com.math3249.listler.ui.viewmodel.ListDetailViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.utilinterface.Swipeable

class ListHistoryFragment : Fragment(), Swipeable<Item> {

    private val navArgs: ListHistoryFragmentArgs by navArgs()
    private var _binding: FragmentListHistoryBinding? = null
    private val binding get() = _binding!!
    val viewModel: ListDetailViewModel by activityViewModels() {
        ListDetailViewModel.ListDetailViewModelFactory (
            (activity?.application as App).database.listDetailDao()
        )
    }

    private var _adapter: ListHistoryAdapter = ListHistoryAdapter()
    private val adapter get() = _adapter

    private var _items = emptyList<Item>()
    private val items get() = _items

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListHistoryBinding.inflate(inflater, container, false)
        _adapter = ListHistoryAdapter()
        setHasOptionsMenu(false)
        prepareActionBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getListItemHistory(navArgs.listDetails.listId).observe(this.viewLifecycleOwner) { listItem ->
            adapter.updateList(listItem.items)
            _items = listItem.items
        }
        swipe()
        subscribeToChildFragment()
        binding.apply {
            historyRecycler.adapter = adapter
        }
    }

    private fun subscribeToChildFragment() {
        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            val message = bundle.get(KEY_INPUT) as Message
            if (message.success) {
                viewModel.deleteItemFromList(
                    ListCategoryItemCrossRef(
                        navArgs.listDetails.listId,
                        -1,
                        message.getId(ITEM_ID)
                    )
                )
                findSearchView(binding.toolbar).setQuery("", false)
                adapter.notifyItemRemoved(message.getId(POSITION).toInt())
            }
        }
    }

    private fun swipe() {
        val itemTouchHelper = ItemTouchHelper(
            DragSwipe(this)
        )
        itemTouchHelper.attachToRecyclerView(binding.historyRecycler)
    }

    private fun prepareActionBar() {
        (activity as MainActivity).supportActionBar?.hide()
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.menu_search)
        findSearchView(toolbar).setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                adapter.filter.filter(text)
                return true
            }
        })
    }

    private fun findSearchView(toolbar: MaterialToolbar): SearchView {
        val searchItem = toolbar.menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        return searchView
    }

    override val swipeDirs: Int
        get() = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    override val parentContext: Context
        get() = requireContext()
    override val listAdapter: ListAdapter<Item, RecyclerView.ViewHolder>
        get() = adapter as ListAdapter<Item, RecyclerView.ViewHolder>

    override fun swipeLeft(position: Int) {
        val item = adapter.getSelectedItem(position)
        DeleteDialog(item.name, item.itemId, position.toLong()).show(childFragmentManager, DeleteDialog.TAG)
    }

    override fun swipeRight(position: Int) {
        val item = adapter.getSelectedItem(position)
    }

}