package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.math3249.listler.App
import com.math3249.listler.databinding.FragmentAddCategoryBinding
import com.math3249.listler.ui.viewmodel.CategoryViewModel

class AddCategoryFragment: Fragment() {

    private val navArgs: AddCategoryFragmentArgs by navArgs()

    private val viewModel: CategoryViewModel by activityViewModels {
        CategoryViewModel.AddCategoryViewModelFactory (
            (activity?.application as App).database.categoryDao()
        )
    }

    private var _binding: FragmentAddCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.apply {
            categoryInput.setText(navArgs.categoryName)
            addCategory.setOnClickListener {
                addCategoryToDatabase()
            }
        }
    }

    private fun addCategoryToDatabase() {
        /*val name = StringUtil.standardizeItemName(binding.categoryInput.text.toString())
        if (name != null) {
            viewModel.addCategory(name)
            viewModel.insertId.observe(this.viewLifecycleOwner) {
                if (it != null) {
                    val action = AddCategoryFragmentDirections
                        .actionAddCategoryFragmentToAddItemFragment(
                            navArgs.listId
                                , navArgs.itemId
                                , navArgs.itemName
                                , it
                                , name)

                    findNavController().navigate(action)
                } else {
                    Utils.snackbar(MessageType.CATEGORY_IN_DATABASE, binding.root, name)
                }
            }
        } else Utils.snackbar(MessageType.CATEGORY_INPUT_EMPTY, binding.root)
        */
    }
}