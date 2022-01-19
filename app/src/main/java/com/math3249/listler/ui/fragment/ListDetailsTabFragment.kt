package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListDetailsTabBinding
import com.math3249.listler.ui.adapter.ListDetailsTabAdapter
import com.math3249.listler.ui.adapter.UNFINISHED_ITEMS

class ListDetailsTabFragment : Fragment() {

    private val navArgs: ListDetailsTabFragmentArgs by navArgs()

    private var _binding: FragmentListDetailsTabBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListDetailsTabBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager
        viewPager.adapter = ListDetailsTabAdapter(this, navArgs.listData)
        setupActionBar()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()

        return binding.root
    }

    private fun getTabTitle(position: Int): String {
        return when (position) {
            UNFINISHED_ITEMS -> navArgs.listData.listName
            else -> getString(R.string.title_finished_items)
        }
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = binding.toolbar
        toolbar.setNavigationIcon(R.drawable.ic_back_24)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        toolbar.inflateMenu(R.menu.menu_list_details)
    }
}