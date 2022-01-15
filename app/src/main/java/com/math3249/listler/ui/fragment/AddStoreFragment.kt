package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentAddStoreBinding
import com.math3249.listler.model.entity.Store
import com.math3249.listler.ui.viewmodel.StoreViewModel
import com.math3249.listler.util.ADD_STORE_FRAGMENT
import com.math3249.listler.util.MenuUtil
import com.math3249.listler.util.STORE_ID
import com.math3249.listler.util.message.type.MessageType

class AddStoreFragment : Fragment() {

    val viewModel: StoreViewModel by activityViewModels {
        StoreViewModel.StoreViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    private var _binding: FragmentAddStoreBinding?  = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeToMessage()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        MenuUtil().prepareMenu(menu, ADD_STORE_FRAGMENT)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_store, menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save_store -> {
                viewModel.addStore(
                    Store(name = binding.nameInput.text.toString())
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun subscribeToMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            if (!message.read) {
                when (message.type) {
                    MessageType.STORE_INSERTED -> {
                        val action = AddStoreFragmentDirections
                            .actionAddStoreFragmentToStoreDetailsFragment(message.getId(STORE_ID), message.extra)
                        findNavController().navigate(action)
                    }
                }
                message.clear()
            }
        }
    }
}