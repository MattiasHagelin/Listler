package com.math3249.listler.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.math3249.listler.ui.fragment.CompletedDetailsFragment
import com.math3249.listler.ui.fragment.ListDetailsFragment
import com.math3249.listler.ui.fragment.navargs.ListDetailsArgs

const val UNFINISHED_ITEMS = 0
const val FINISHED_ITEMS = 1
class ListDetailsTabAdapter(
    fragment: Fragment,
    navArgs: ListDetailsArgs
): FragmentStateAdapter(fragment) {

    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        UNFINISHED_ITEMS to {ListDetailsFragment(navArgs)},
        FINISHED_ITEMS to {CompletedDetailsFragment(navArgs.listId)}
    )

    override fun getItemCount() =tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}