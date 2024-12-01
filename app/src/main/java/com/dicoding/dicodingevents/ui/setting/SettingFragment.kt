package com.dicoding.dicodingevents.ui.setting

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.dicoding.dicodingevents.R
import com.dicoding.dicodingevents.databinding.FragmentSettingBinding
import com.dicoding.dicodingevents.ui.notification.Worker
import com.dicoding.dicodingevents.viewmodel.EventViewModel
import com.dicoding.dicodingevents.viewmodel.EventViewModelFactory
import java.util.concurrent.TimeUnit

class SettingFragment : PreferenceFragmentCompat() {
    private var _binding: FragmentSettingBinding? = null
    private val viewmodel by viewModels<EventViewModel> {
        EventViewModelFactory.getInstance(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val themeButton = findPreference<SwitchPreference>("themeKey")
        val notificationButton = findPreference<SwitchPreference>("notificationKey")

        viewmodel.getThemeSettings().observe(viewLifecycleOwner) { isDark ->
            themeButton?.isChecked = isDark
        }
        viewmodel.getNotificationSetting().observe(viewLifecycleOwner) { isActive ->
            notificationButton?.isChecked = isActive
        }

        themeButton?.setOnPreferenceChangeListener { _, newValue ->
            val isDarkMode = newValue as Boolean
            viewmodel.saveThemeSetting(isDarkMode)
            setTheme(isDarkMode)
            true
        }

        notificationButton?.setOnPreferenceChangeListener { _, newValue ->
            val isEnabled = newValue as Boolean
            viewmodel.saveNotificationSetting(isEnabled)
            if (isEnabled) {
                startPeriodicTask()
            } else {
                WorkManager.getInstance(requireContext()).cancelUniqueWork(Worker.NOTIFICATION_ID.toString())
            }
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    private fun setTheme(isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun startPeriodicTask() {
        val workRequest: WorkRequest = PeriodicWorkRequestBuilder<Worker>(1, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(requireContext()).enqueue(workRequest)
    }
}
