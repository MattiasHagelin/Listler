package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoresBinding
import com.math3249.listler.model.entity.Store
import com.math3249.listler.ui.adapter.StoresAdapter
import com.math3249.listler.ui.fragment.dialog.AddStoreDialog
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.message.StoreMessage
import com.math3249.listler.util.utilinterface.Swipeable

class StoresFragment : Fragment(), Swipeable<Store> {

    private val viewModel: StoreViewModel by activityViewModels {
        StoreViewModel.StoreViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    private var _binding: FragmentStoresBinding? = null
    val binding get() = _binding!!

    private var _adapter: StoresAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        _adapter = createStoreAdapter()
        setHasOptionsMenu(true)
        prepareActionBar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToStores()
        subscribeToMessage()
        swipe()
        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            val message = bundle.get(KEY_INPUT) as StoreMessage
            if (message.success)
                viewModel.addStore(Store(name = message.store.name))
             else
                Utils.snackbar(message.type, binding.root, getString(R.string.store))
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

    private fun subscribeToStores() {
        viewModel.stores.observe(this.viewLifecycleOwner) { stores ->
            val storesWithoutDef = stores.filter { it.storeId != 1L }
            adapter.submitList(storesWithoutDef)
        }
    }

    private fun subscribeToMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            message as StoreMessage
            when (message.type) {
                Message.Type.STORE_INSERTED -> {
                    val action = StoresFragmentDirections
                        .actionStoresFragmentToStoreDetailsFragment(
                            message.store.storeId,
                            message.store.name
                        )
                    findNavController().navigate(action)
                }
                Message.Type.STORE_NEW -> {
                    viewModel.addStore(Store(name = message.store.name))
                }
                else -> {}
            }
            message.clear()
        }
    }

    private fun swipe() {
        val itemTouchHelper = ItemTouchHelper(DragSwipe(this))
        itemTouchHelper.attachToRecyclerView(binding.storesRecyclerview)
    }

    private fun prepareActionBar() {
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar?.show()
        actionBar?.title = getString(R.string.title_stores)
    }

    override val swipeDirs: Int
        get() = LEFT
    override val parentContext: Context
        get() = requireContext()
    override val listAdapter: ListAdapter<Store, RecyclerView.ViewHolder>
        get() = adapter as ListAdapter<Store, RecyclerView.ViewHolder>

    override fun swipeLeft(position: Int) {
        viewModel.delete(adapter.getSelectedStore(position))
    }

    override fun swipeRight(position: Int) {
        return
    }
}