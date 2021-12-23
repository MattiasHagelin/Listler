package com.math3249.listler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.math3249.listler.App
import com.math3249.listler.databinding.FragmentAddCategoryBinding
import com.math3249.listler.ui.viewmodel.AddCategoryViewModel

class AddCategoryFragment: Fragment() {

    private val navArgs: AddCategoryFragmentArgs by navArgs()

    private val viewModel: AddCategoryViewModel by activityViewModels {
        AddCategoryViewModel.AddCategoryViewModelFactory (
            (activity?.application as App).database.categoryDao()
        )
    }

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding
            .inflate(
                inflater,
                container,
                false
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}