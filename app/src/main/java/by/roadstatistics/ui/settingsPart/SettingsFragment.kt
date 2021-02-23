package by.roadstatistics.ui.settingsPart

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.adapters.SettingsSpinnerAdapter
import by.roadstatistics.databinding.FragmentSettingsBinding
import by.roadstatistics.utils.Constants.MAP_LOOP

// настрйоки

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        viewModelProvider = ViewModelProvider(this)
        binding.loopState.text = MAP_LOOP.toString()

        binding.plusLoopSize.setOnClickListener {
            if (MAP_LOOP <= 15) {
                MAP_LOOP += 1
                binding.loopState.text = MAP_LOOP.toString()
            }
        }

        binding.minusLoopSize.setOnClickListener {
            if (MAP_LOOP >= 2) {
                MAP_LOOP -= 1
                binding.loopState.text = MAP_LOOP.toString()
            }
        }

        viewModelProvider.get(SettingsViewModel::class.java).also { viewModel ->
            viewModel.yearListLiveData.observe(
                viewLifecycleOwner,
                { yearList ->
                    view.findViewById<Spinner>(R.id.settingsYearSpinner).apply {
                        adapter = SettingsSpinnerAdapter(view.context, R.id.dayOfWeek, yearList)
                    }

                })
            viewModel.getYearList(view.context)
        }


    }


}