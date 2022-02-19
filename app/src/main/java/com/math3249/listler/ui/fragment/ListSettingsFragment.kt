package com.math3249.listler.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.math3249.listler.App
import com.math3249.listler.R
import com.math3249.listler.databinding.FragmentListSettingsBinding
import com.math3249.listler.model.entity.ListSettings
import com.math3249.listler.ui.fragment.dialog.RadioListDialog
import com.math3249.listler.ui.viewmodel.ListViewModel
import com.math3249.listler.util.KEY_ENTRY_TYPE
import com.math3249.listler.util.KEY_ENTRY_VALUE
import com.math3249.listler.util.KEY_REQUEST

class ListSettingsFragment:  Fragment() {

    private val navArgs: ListSettingsFragmentArgs by navArgs()
    private val viewModel: ListViewModel by activityViewModels() {
        ListViewModel.ListViewModelFactory(
            (activity?.application as App).database.listDao()
        )
    }

    private var _listSettings: ListSettings? = null
    private val listSettings get() = _listSettings!!

    private var _binding: FragmentListSettingsBinding?  = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListSettingsBinding.inflate(inflater, container, false)
        //setHasOptionsMenu(false)
        prepareActionbar()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToListSettings()
        subscribeToStores()
        subscribeToChildFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        /*
        val timeLimit = findPreference<ListPreference>(getString(R.string.key_completed_item_time_limit))?.value?.toIntOrNull() ?: 1
        val homeScreen = findPreference<SwitchPreference>(getString(R.string.key_home_screen))?.isChecked ?: false
        val store = findPreference<ListPreference>(getString(R.string.key_store_sort_order))?.value?.toLongOrNull() ?: -1
        viewModel.insertOrUpdate(
            ListSettings(
            navArgs.listArgs.listId,
            timeLimit,
            homeScreen,
            store
        ))*/
    }

    private fun prepareActionbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = binding.listSettingsToolbar.standardToolbar
        toolbar.title = navArgs.listArgs.listName + " settings"
        toolbar.setNavigationIcon(R.drawable.ic_back_24)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun subscribeToListSettings() {
        viewModel.getListSettings(navArgs.listArgs.listId).observe(this.viewLifecycleOwner) {
            _listSettings = it
            bind()
        }
    }

    private fun subscribeToStores() {
        viewModel.stores.observe(this.viewLifecycleOwner) { stores ->
            val entries = stores.map { s -> s.name }.toTypedArray()
            val entryValues = stores.map { s -> s.storeId.toString() }.toTypedArray()

            binding.apply {
                settingStoreSortHeader.text = getString(R.string.store_sort_order_title)
                settingStoreSortSummary.text = getString(R.string.store_sort_order_summary)
                settingStoreSortCard.setOnClickListener {
                    RadioListDialog(
                        //TODO: Fix default values
                        entries,
                        entryValues,
                        listSettings.storeId.toInt(),
                        R.string.store_sort_order_title
                    ).show(childFragmentManager, RadioListDialog.TAG)
                }
            }
        }
    }

    private fun subscribeToChildFragment() {
        childFragmentManager.setFragmentResultListener(KEY_REQUEST, this.viewLifecycleOwner) { _, bundle ->
            when (bundle.get(KEY_ENTRY_TYPE)){
                R.string.store_sort_order_title -> {
                    viewModel.insertOrUpdate(
                        ListSettings(
                        listSettings.listId,
                            listSettings.completeTimeLimit,
                            listSettings.isHomeScreen,
                            bundle.get(KEY_ENTRY_VALUE).toString().toLong()
                    ))
                }
                else -> {
                    viewModel.insertOrUpdate(
                        ListSettings(
                            listSettings.listId,
                            bundle.get(KEY_ENTRY_VALUE).toString().toInt(),
                            listSettings.isHomeScreen,
                            listSettings.storeId
                    ))
                }
            }
        }
    }

    private fun bind() {
        binding.apply {
            settingTimeLimitHeader.text = getString(R.string.completed_item_time_limit_title)
            settingTimeLimitSummary.text = getString(R.string.completed_item_time_limit_summary)
            settingTimeLimitCard.setOnClickListener {
                RadioListDialog(
                    resources.getStringArray(R.array.completedItemTimeLimit),
                    resources.getStringArray(R.array.completedItemTimeLimitData),
                    listSettings.completeTimeLimit,
                    R.string.completed_item_time_limit_title
                ).show(childFragmentManager, RadioListDialog.TAG)
            }

            settingHomeScreenHeader.text = getString(R.string.is_home_screen_title)
            settingHomeScreenSummary.text = getString(R.string.is_home_screen_summary)
            settingHomeScreenSwitch.isChecked = false//listSettings.isHomeScreen
            settingHomeScreenSwitch.setOnCheckedChangeListener { _, isChecked ->
                viewModel.insertOrUpdate(
                    ListSettings(
                        listSettings.listId,
                        listSettings.completeTimeLimit,
                        isChecked,
                        listSettings.storeId
                ))
            }



        }
    }

    private fun prepareViewText(header: TextView, summary: TextView, headerText: String, summaryText: String) {
        header.text = headerText
        summary.text = summaryText
    }
}