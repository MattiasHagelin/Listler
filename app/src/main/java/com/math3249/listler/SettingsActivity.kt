package com.math3249.listler

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.math3249.listler.databinding.ActivitySettingsBinding
import com.math3249.listler.ui.fragment.SettingsFragment
import com.math3249.listler.util.StringUtil

class SettingsActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = StringUtil.getString(R.string.settings_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        if (binding.settingsLayout != null) {
            if (savedInstanceState != null) {
                return
            }
            // below line is to inflate our fragment.
            supportFragmentManager.beginTransaction().replace(R.id.settings_layout, SettingsFragment()).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (android.R.id.home == id) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}