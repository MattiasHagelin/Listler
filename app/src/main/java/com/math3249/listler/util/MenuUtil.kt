package com.math3249.listler.util

import android.view.Menu
import android.view.MenuItem
import com.math3249.listler.R

class MenuUtil {
        fun hide(item: MenuItem){
            item.isVisible = false
            item.isEnabled = false
        }

        fun show(item: MenuItem) {
            item.isVisible = true
            item.isEnabled = true
        }

        fun prepareMenu(menu: Menu, fragment: String) {
            when (fragment) {
                LIST_DETAIL_FRAGMENT -> {
                    hide(menu.findItem(R.id.action_settings))
                    show(menu.findItem(R.id.list_details_to_completed_items))
                }
            }

        }

        fun toggle(item: MenuItem) {
            if (item.isVisible) hide(item)
            else show(item)
        }
}