package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
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
import com.math3249.listler.databinding.FragmentListOverviewBinding
import com.math3249.listler.ui.adapter.ListOverviewAdapter
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.viewmodel.ListOverviewViewModel
import com.math3249.listler.util.DragSwipe
import com.math3249.listler.util.INPUT_KEY
import com.math3249.listler.util.KEY_LIST_TYPE
import com.math3249.listler.util.REQUEST_KEY
import com.math3249.listler.util.dialogs.AddListDialog
import com.math3249.listler.util.dialogs.InputDialog

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
    ): View {
        _binding = FragmentListOverviewBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ListOverviewAdapter {
            list -> val action = ListOverviewFragmentDirections
            .actionListOverviewFragmentToListDetailsTabFragment2(
                ListDetailsArgs(
                    list.listId,
                    list.name
                )
            )
            findNavController().navigate(action)/*val action = ListOverviewFragmentDirections
            .actionListOverviewFragmentToListDetailsFragment(list.listId)
            findNavController().navigate(action)
            */
        }

        viewModel.allLists.observe(this.viewLifecycleOwner) {
            list -> list.let {
                adapter.submitList(it)
            }
        }

        childFragmentManager.setFragmentResultListener(REQUEST_KEY, this.viewLifecycleOwner) { _, bundle ->
            viewModel.addList(
                name = bundle.getString(INPUT_KEY)!!,
                type = bundle.getString(KEY_LIST_TYPE)!!
            )
        }

        swipe(viewModel, adapter)

        binding.apply {
            listOverviewRecyclerview.adapter = adapter
            addNewListButton.setOnClickListener {
                AddListDialog().show(childFragmentManager, InputDialog.TAG)
                //findNavController().navigate(
                //    R.id.action_listOverviewFragment_to_addListFragment
                //)
            }
        }
    }

    private fun swipe(viewModel: ListOverviewViewModel, adapter: ListOverviewAdapter){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(
            icon = AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_delete_24),// Get Icon
            swipeDirs = ItemTouchHelper.LEFT,
            swipeLeft = { position -> viewModel.deleteList(adapter.getItemsItemId(position)) }))
        itemTouchHelper.attachToRecyclerView(binding.listOverviewRecyclerview)
    }
}