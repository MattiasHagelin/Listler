package com.math3249.listler.util

import android.view.Menu
import android.view.MenuItem
import com.math3249.listler.R

class MenuUtil {
    private val actionSettings = R.id.action_settings
    private val toStoreManagement = R.id.to_store_management
    private val listDetailsToCompletedItems = R.id.list_details_to_completed_items

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
            LIST_DETAIL_FRAGMENT -> hideListOverViewMenuItems(menu)
            ADD_STORE_FRAGMENT -> hideListOverViewMenuItems(menu)
            STORE_DETAILS_FRAGMENT -> hideListOverViewMenuItems(menu)
            STORES_FRAGMENT -> hideListOverViewMenuItems(menu)
        }

    }

    private fun hideListOverViewMenuItems(menu: Menu) {
        hide(menu.findItem(toStoreManagement))
        hide(menu.findItem(actionSettings))
    }

    fun toggle(item: MenuItem) {
        if (item.isVisible) hide(item)
        else show(item)
    }
}