package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoresBinding
import com.math3249.listler.model.entity.Store
import com.math3249.listler.ui.adapter.StoresAdapter
import com.math3249.listler.ui.fragment.dialog.AddStoreDialog
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.type.MessageType

class StoresFragment : Fragment() {

    private val viewModel: StoreViewModel by activityViewModels {
        StoreViewModel.StoreViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    private var _binding: FragmentStoresBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        prepareActionBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = createStoreAdapter()
        subscribeToStores(adapter)
        subscribeToMessage()
        swipe(adapter)
        childFragmentManager.setFragmentResultListener(REQUEST_KEY, this.viewLifecycleOwner) { _, bundle ->
            val error = bundle.getString(KEY_ERROR)
            if (error == null) {
                val input = bundle.getString(INPUT_KEY)
                viewModel.addStore(Store(name = input!!))
            } else
                Utils.snackbar(MessageType.INVALID_INPUT, binding.root)
        }

        binding.apply {
            storesRecyclerview.adapter = adapter
            addStoreButton.setOnClickListener {
                AddStoreDialog().show(childFragmentManager, AddStoreDialog.TAG)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        MenuUtil.prepareMenu(menu, STORES_FRAGMENT)
    }

    private fun createStoreAdapter(): StoresAdapter {
        return StoresAdapter { store ->
            val action = StoresFragmentDirections
                .actionStoresFragmentToStoreDetailsFragment(store.storeId, store.name)
            findNavController().navigate(action)
        }
    }

    private fun subscribeToStores(adapter: StoresAdapter) {
        viewModel.stores.observe(this.viewLifecycleOwner) { stores ->
            adapter.submitList(stores)
        }
    }

    private fun subscribeToMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            if (!message.read) {
                when (message.type) {
                    MessageType.STORE_INSERTED -> {
                        val action = StoresFragmentDirections
                            .actionStoresFragmentToStoreDetailsFragment(message.getId(STORE_ID), message.extra)
                        findNavController().navigate(action)
                    }
                    MessageType.STORE_NEW -> {
                        viewModel.addStore(Store(name = message.extra))
                    }
                    else -> {}
                }
                message.clear()
            }
        }
    }

    private fun swipe(adapter: StoresAdapter) {
        val itemTouchHelper = ItemTouchHelper(DragSwipe(
            swipeDirs = ItemTouchHelper.LEFT,
            icon = AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_delete_24),
            swipeLeft = { position ->
                val item = adapter.getItemViewType(position)
                viewModel.delete(adapter.getSelectedStore(position))
            }))
        itemTouchHelper.attachToRecyclerView(binding.storesRecyclerview)
    }

    private fun prepareActionBar() {
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.show()
        actionBar?.title = getString(R.string.title_stores)
    }
}