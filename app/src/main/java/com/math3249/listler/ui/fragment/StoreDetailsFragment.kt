package com.math3249.listler.ui.fragment

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoreDetailsBinding
import com.math3249.listler.model.StoreCategoryWithCategoryName
import com.math3249.listler.ui.adapter.StorDetailsAdapter
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message.Type
import com.math3249.listler.util.utilinterface.ItemTouchHelperAdapter
import com.math3249.listler.util.utilinterface.OnStartDragListener


class StoreDetailsFragment : Fragment() {

    private val navArgs: StoreDetailsFragmentArgs by navArgs()
    private val viewModel: StoreViewModel by activityViewModels {
        StoreViewModel.StoreViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    var itemTouchHelper: ItemTouchHelper? = null

    private var _binding: FragmentStoreDetailsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: StorDetailsAdapter? = null
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
        subscribeToStoreCategories()
        subscribeToCategories()
        binding.apply {
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
                viewModel.updateSortOrder(navArgs.storeId, adapter.getList())
                findNavController().navigate(R.id.action_storeDetailsFragment_to_storesFragment)
            }
        }
    }

    private fun createAdapter(storeCategories: List<StoreCategoryWithCategoryName>) {
        _adapter = StorDetailsAdapter(
            object:ItemTouchHelperAdapter{
                override fun onItemMove(from: Int, to: Int): Boolean {
                    return false
                }

                override fun onItemDismiss(position: Int) {
                    viewModel.deleteCategory(storeCategories[position].storeCat)
                }

            },
        object:OnStartDragListener{
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                itemTouchHelper!!.startDrag(viewHolder!!)
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        MenuUtil.prepareMenu(menu, STORE_DETAILS_FRAGMENT)
    }

    private fun subscribeToStoreCategories() {
        viewModel.getStoreCategories(navArgs.storeId).observe(this.viewLifecycleOwner) {
            storeCategories ->
            if (_adapter == null) {
                createAdapter(storeCategories)
                binding.apply {
                    val layoutManager = LinearLayoutManager(requireContext())
                    categoryRecyclerview.layoutManager = layoutManager
                    categoryRecyclerview.adapter = adapter
                }
                val callback = ListItemTouchHelperCallback(adapter,
                    dragable = true,
                    swipable = true
                )
                itemTouchHelper = ItemTouchHelper(callback)
                itemTouchHelper!!.attachToRecyclerView(binding.categoryRecyclerview)

            }
            adapter.clearData()
            adapter.insertData(storeCategories.toMutableList())


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
        viewModel.addCategoryToStore(navArgs.storeId, input, calculateNextSortOrder())
    }

    internal object Compare{
        fun max(first: StoreCategoryWithCategoryName,
                second: StoreCategoryWithCategoryName): StoreCategoryWithCategoryName {
            return if (first.storeCat.sortOrder > second.storeCat.sortOrder) first
                else second
        }
    }

    private fun calculateNextSortOrder(): Long {
        val maxSort = adapter.getList().reduceOrNull(Compare::max)
        return  maxSort?.storeCat?.sortOrder?.plus(1) ?: 0
    }
}