package com.math3249.listler.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.math3249.listler.App
import com.math3249.listler.MainActivity
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListOverviewBinding
import com.math3249.listler.model.entity.List
import com.math3249.listler.ui.adapter.ListOverviewAdapter
import com.math3249.listler.ui.fragment.dialog.AddListDialog
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.viewmodel.ListViewModel
import com.math3249.listler.util.*
import com.math3249.listler.util.message.ListMessage
import com.math3249.listler.util.message.Message
import com.math3249.listler.util.utilinterface.Swipeable

class ListOverviewFragment : Fragment(), Swipeable<List> {
    private val viewModel: ListViewModel by activityViewModels {
        ListViewModel.ListViewModelFactory(
            (activity?.application as App).database.listDao()
        )
    }

    private var _binding: FragmentListOverviewBinding? = null
    private val binding get() = _binding!!

    private var _adapter: ListOverviewAdapter? = null
    private val adapter get() = _adapter!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOverviewBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        _adapter = ListOverviewAdapter { list ->
            val action = com.math3249.listler.ui.fragment.ListOverviewFragmentDirections
            .actionListOverviewFragmentToListDetailsTabFragment2(
                ListDetailsArgs(
                    list.listId,
                    list.name
                )
            )
            findNavController().navigate(action)
        }
        (activity as MainActivity).supportActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToMessage()

        viewModel.allLists.observe(this.viewLifecycleOwner) {
            list -> list.let {
                adapter.submitList(it)
            }
        }

        //viewModel.getHomeScreen()

        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            val message = bundle.get(KEY_INPUT) as ListMessage
            if (message.success)
                viewModel.addList(
                    name = message.listData.listName,
                    type = message.listData.listType
                )
            else
                Utils.snackbar(message.type!!, binding.root, getString(R.string.list))
        }

        swipe()

        binding.apply {
            listOverviewRecyclerview.adapter = adapter
            addNewListButton.setOnClickListener {
                AddListDialog().show(childFragmentManager, AddListDialog.TAG)
            }
        }
    }

    private fun subscribeToMessage() {
        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            if (message.type != Message.Type.NO_MESSAGE) {
                message as ListMessage
                var action: NavDirections
                when (message.type) {
                    Message.Type.LIST_INSERTED -> {
                        action = ListOverviewFragmentDirections
                            .actionListOverviewFragmentToListDetailsTabFragment2(
                                ListDetailsArgs(
                                    message.listData.listItem.listId,
                                    message.listData.listName
                                )
                            )
                        findNavController().navigate(action)
                    }
                    Message.Type.LIST_HOME_SCREEN -> {
                        action = ListOverviewFragmentDirections
                            .actionListOverviewFragmentToListDetailsTabFragment2(
                                ListDetailsArgs(
                                    message.listData.listItem.listId,
                                    message.listData.listName
                                )
                            )
                        findNavController().navigate(action)
                    }
                    else -> {}
                }
                message.clear()
            }
        }
    }

    private fun swipe(){
        val itemTouchHelper = ItemTouchHelper(DragSwipe(this))
        itemTouchHelper.attachToRecyclerView(binding.listOverviewRecyclerview)
    }

    override val swipeDirs: Int
        get() = LEFT
    override val parentContext: Context
        get() = requireContext()
    override val listAdapter: ListAdapter<List, RecyclerView.ViewHolder>
        get() = adapter as ListAdapter<List, RecyclerView.ViewHolder>

    override fun swipeLeft(position: Int) {
        viewModel.deleteList(adapter.getItemsItemId(position))
    }

    override fun swipeRight(position: Int) {
        return
    }
}