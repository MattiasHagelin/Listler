package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoreDetailsBinding
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.model.crossref.StoreCategoryCrossRef
import com.math3249.listler.ui.adapter.StoreDetailsAdapter
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.utilinterface.Dragable
import com.math3249.listler.util.utilinterface.Swipeable


class StoreDetailsFragment : Fragment(), Swipeable<StoreCategoryWithCategoryName>, Dragable {

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
        setHasOptionsMenu(false)
        (activity as MainActivity).supportActionBar?.hide()
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
                    addCategoryToStore(input)
                else
                    Utils.snackbar(Type.INVALID_INPUT, binding.categoryRecyclerview)
                Utils.clearDropdown(binding.categoryDropdown)
            }

            categoryDropdown.setOnItemClickListener { _, _, _, _ ->
                addCategoryToStore(categoryDropdown.text.toString())
                Utils.clearDropdown(categoryDropdown)
            }
            //actionBar.title.text = navArgs.storeName
            actionBar.cancelButton.setOnClickListener {
                findNavController().navigate(R.id.action_storeDetailsFragment_to_storesFragment)
            }
            actionBar.saveButton.setOnClickListener {
                //Persist movement changes when the fragment is destroyed
                viewModel.updateSortOrder(navArgs.storeId, adapter.list)
                findNavController().navigate(R.id.action_storeDetailsFragment_to_storesFragment)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        MenuUtil.prepareMenu(menu, STORE_DETAILS_FRAGMENT)
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

    private fun addCategoryToStore(input: String) {
        viewModel.addCategoryToStore(navArgs.storeId, input, calculateNextSortOrder()
            )
    }

    private fun swipe(adapter: StoreDetailsAdapter) {
        val dragSwipe = DragSwipe(this, this)
        val itemTouchHelper = ItemTouchHelper(dragSwipe)
        itemTouchHelper.attachToRecyclerView(binding.categoryRecyclerview)
    }

    internal object Compare{
        fun max(first: StoreCategoryWithCategoryName,
                second: StoreCategoryWithCategoryName): StoreCategoryWithCategoryName {
            return if (first.storeCat.sortOrder > second.storeCat.sortOrder) first
                else second
        }
    }

    private fun calculateNextSortOrder(): Long {
        val maxSort = adapter.list.reduceOrNull(Compare::max)
        return  maxSort?.storeCat?.sortOrder?.plus(1) ?: 0
    }

    override val dragDirs: Int
        get() = UP or DOWN

    override fun movement(from: Int, to: Int) {
        adapter.moveItem(from, to)
        adapter.notifyItemMoved(from, to)
    }

    override val swipeDirs: Int
        get() = LEFT
    override val parentContext: Context
        get() = requireContext()
    override val listAdapter: ListAdapter<StoreCategoryWithCategoryName, RecyclerView.ViewHolder>
        get() = adapter as ListAdapter<StoreCategoryWithCategoryName, RecyclerView.ViewHolder>

    override fun swipeLeft(position: Int) {
        val item = adapter.currentList[position]
        adapter.removeItem(position)
        viewModel.deleteCategory(
            StoreCategoryCrossRef(
                navArgs.storeId,
                item.storeCat.categoryId
            )
        )
    }

    override fun swipeRight(position: Int) {
        return
    }
}