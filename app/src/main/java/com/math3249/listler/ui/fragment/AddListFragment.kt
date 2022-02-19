package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentAddListBinding
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs
import com.math3249.listler.ui.viewmodel.ListViewModel
import com.math3249.listler.util.Type
import com.math3249.listler.util.message.ListMessage

class AddListFragment: Fragment() {

    private val viewModel: ListViewModel by activityViewModels {
        ListViewModel.ListViewModelFactory(
            (activity?.application as App).database.listDao()
        )
    }

    private var _binding: FragmentAddListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // val id = navArgs.id

        //Create a string array from enum of list types
        val types = enumValues<Type>()
        val typesAsString = arrayOfNulls<String>(types.count())
        for (i in types.indices) {
            typesAsString[i] = types[i].toString()
        }


        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_dropdown, typesAsString)
        (binding.listTypeDropdown as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.listTypeDropdown.setText(typesAsString[0], false)

        viewModel.message.observe(this.viewLifecycleOwner) { message ->
            message as ListMessage
            val action = AddListFragmentDirections
                .actionAddListFragmentToListDetailsTabFragment(
                    ListDetailsArgs(message.listData.listItem.listId, binding.nameInput.text.toString()))
                findNavController().navigate(action)
        }

        binding.addList.setOnClickListener {
            viewModel.addList(
                binding.nameInput.text.toString(),
                binding.listTypeDropdown.text.toString()
            )
            /*
            findNavController().navigate(
                R.id.action_addListFragment_to_listOverviewFragment
            )

             */
        }

    }
}