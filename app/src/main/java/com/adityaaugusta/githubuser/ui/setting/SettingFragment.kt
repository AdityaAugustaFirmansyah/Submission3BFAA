package com.adityaaugusta.githubuser.ui.setting

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adityaaugusta.githubuser.R
import com.adityaaugusta.githubuser.background.AlarmReceiver
import com.adityaaugusta.githubuser.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var fragmentSettingBinding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentSettingBinding = FragmentSettingBinding.inflate(inflater, container, false)
        return fragmentSettingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        fragmentSettingBinding.btnTranslate.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        fragmentSettingBinding.apply {

            val alarmReceiver = AlarmReceiver()

            switchReminder.isChecked = context?.let { alarmReceiver.isAlarmActive(it) } == true

            if (context?.let { alarmReceiver.isAlarmActive(it) } == true) {
                switchReminder.text = getString(R.string.on)
            } else {
                switchReminder.text = getString(R.string.off)
            }

            toolbar4.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            switchReminder.setOnCheckedChangeListener { _, b ->
                if (b) {
                    Toast.makeText(
                        context,
                        getString(R.string.reminder_enabled),
                        Toast.LENGTH_SHORT
                    ).show()
                    switchReminder.text = getString(R.string.on)
                    context?.let { alarmReceiver.setRepeatingAlarm(it) }
                } else {
                    Toast.makeText(
                        context,
                        getString(R.string.reminder_disable),
                        Toast.LENGTH_SHORT
                    ).show()
                    switchReminder.text = getString(R.string.off)
                    context?.let { alarmReceiver.cancelAlarm(it) }
                }
            }
        }
    }
}
