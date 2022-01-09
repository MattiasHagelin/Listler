package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.math3249.listler.App
import com.math3249.listler.databinding.FragmentStoreDetailsBinding
import com.math3249.listler.ui.viewmodel.StoreDetailsViewModel


class StoreDetailsFragment : Fragment() {

    private val viewModel: StoreDetailsViewModel by activityViewModels() {
        StoreDetailsViewModel.StoreDetailsViewModelFactory(
            (activity?.application as App).database.storeDao()
        )
    }

    private var _binding: FragmentStoreDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoreDetailsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }
}