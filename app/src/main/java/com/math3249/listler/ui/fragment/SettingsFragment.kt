package com.math3249.listler.ui.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.math3249.listler.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}