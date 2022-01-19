package com.math3249.listler.util

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.math3249.listler.MainActivity
import com.math3249.listler.R

object MenuUtil {
    private val actionSettings = R.id.action_settings
    private val toStoreManagement = R.id.to_store_management

    fun prepareToolBar(activity: MainActivity, toolbar: Toolbar, inflateMenu: Boolean) {
        activity.supportActionBar?.hide()
        activity.setSupportActionBar(toolbar)
        activity.doInflateMenu(inflateMenu)
    }

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
        //hide(menu.findItem(actionSettings))
    }

    fun toggle(item: MenuItem) {
        if (item.isVisible) hide(item)
        else show(item)
    }
}