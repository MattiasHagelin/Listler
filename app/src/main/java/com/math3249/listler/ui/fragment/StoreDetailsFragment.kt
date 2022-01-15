package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoreDetailsBinding
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.ui.adapter.StoreDetailsAdapter
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.type.MessageType


class StoreDetailsFragment : Fragment() {

    private val navArgs: StoreDetailsFragmentArgs by navArgs()
    private val viewModel: StoreViewModel by activityViewModels {
        StoreViewModel.StoreViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    private var _binding: FragmentStoreDetailsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: StoreDetailsAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = StoreDetailsAdapter()
        subscribeToStoreCategories()
        subscribeToCategories()
        swipe(adapter)
        binding.apply {
            categoryRecyclerview.adapter = adapter
            addCategoryToStore.setEndIconOnClickListener{
                val input = StringUtil.standardizeItemName(categoryDropdown.text.toString())
                if (input != null)
                    viewModel.addCategoryToStore(navArgs.storeId, input)
                else
                    Utils.snackbar(MessageType.INVALID_INPUT, binding.categoryRecyclerview)
                Utils.clearItemDropdown(binding.categoryDropdown)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        MenuUtil().prepareMenu(menu, STORE_DETAILS_FRAGMENT)
    }
    override fun onDestroy() {
        super.onDestroy()
        //Persist movement changes when the fragment is destroyed
        viewModel.updateSortOrder(navArgs.storeId, adapter.list)
    }

    private fun subscribeToStoreCategories() {
        viewModel.getStoreCategories(navArgs.storeId).observe(this.viewLifecycleOwner) {
            storeCategories ->
            adapter.submitList(storeCategories)
            (requireActivity() as AppCompatActivity).supportActionBar?.title = navArgs.storeName
        }
    }

    private fun subscribeToCategories() {
        viewModel.categories.observe(this.viewLifecycleOwner) { categories ->
            val arr = categories.map {
                category -> category.name
            }
            binding.categoryDropdown.setAdapter(
                ArrayAdapter(requireContext(), R.layout.add_list_dropdown, arr
            ))
        }
    }

    private fun swipe(adapter: StoreDetailsAdapter) {
        val dragSwipe = DragSwipe(
            dragDirs = ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            swipeDirs = ItemTouchHelper.LEFT,
            icon = AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_delete_24),
            swipeLeft = { position ->
                val item = adapter.currentList[position]
                adapter.removeItem(position)
                viewModel.deleteCategory(
                    StoreCategoryCrossRef(
                        navArgs.storeId,
                        item.storeCat.categoryId
                    )
                )
            },
            onMove = {from, to ->
                adapter.moveItem(from, to)
                adapter.notifyItemMoved(from, to)
            }
        )
        val itemTouchHelper = ItemTouchHelper(dragSwipe)
        itemTouchHelper.attachToRecyclerView(binding.categoryRecyclerview)
    }
}