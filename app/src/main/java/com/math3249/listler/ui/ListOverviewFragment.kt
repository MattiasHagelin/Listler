package com.math3249.listler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListOverviewBinding
import com.math3249.listler.ui.adapter.ListOverviewAdapter
import com.math3249.listler.ui.viewmodel.ListOverviewViewModel
import com.math3249.listler.App

class ListOverviewFragment : Fragment() {
    private val viewModel: ListOverviewViewModel by activityViewModels {
        ListOverviewViewModel.ListOverviewViewModelFactory(
            (activity?.application as App).database.listDao()
        )
    }

    private var _binding: FragmentListOverviewBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListOverviewAdapter {
            list -> val action = ListOverviewFragmentDirections
            .actionListOverviewFragmentToListDetailsFragment(list.listId,list.name)
            findNavController().navigate(action)
        }

        viewModel.allLists.observe(this.viewLifecycleOwner) {
            list -> list.let {
                adapter.submitList(it)
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
            addNewList.setOnClickListener {
                findNavController().navigate(
                    R.id.action_listOverviewFragment_to_addListFragment
                )
            }
        }
    }
}