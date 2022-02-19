package com.math3249.listler

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.math3249.listler.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var inflateMenu = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.actionBar.standardToolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present.
        return if (inflateMenu){
            menuInflater.inflate(R.menu.menu_main, menu)
            true
        } else false
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle action bar item clicks here. The action bar will
        //automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity on AndroidManifest.xml
        return when (item.itemId) {
            R.id.action_settings -> {
                val i = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(i)
                true
            }
            R.id.to_store_management -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_listOverviewFragment_to_storesFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun doInflateMenu(value: Boolean = true) {
        inflateMenu = value
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun setHomeDestination(navController: NavController){
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        val destination: Int? = null //TODO: Replace with destination Id from settings
        navGraph.setStartDestination(destination!!)
        navController.graph = navGraph
    }

}