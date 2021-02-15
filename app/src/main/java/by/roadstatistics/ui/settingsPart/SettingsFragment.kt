package by.roadstatistics.ui.settingsPart

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import by.roadstatistics.adapters.SettingsSpinnerAdapter
import by.roadstatistics.adapters.SpinnerAdapter

// настрйоки

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lad = SettingsSpinnerAdapter(
            context = view.context,
            R.id.dayOfWeek,
            listOf("2020", "2021", "2022", "2023")
        )
        view.findViewById<Spinner>(R.id.settingsYearSpinner).adapter = lad

        lad.notifyDataSetChanged()

    }

}