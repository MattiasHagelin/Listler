package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentStoresBinding
import com.math3249.listler.ui.adapter.StoresAdapter
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.MenuUtil
import com.math3249.listler.util.STORES_FRAGMENT

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
        (activity as MainActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = createStoreAdapter()
        subscribeToStores(adapter)

        binding.apply {
            storesRecyclerview.adapter = adapter
            addStoreButton.setOnClickListener {
                findNavController().navigate(R.id.action_storesFragment_to_addStoreFragment)
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
}