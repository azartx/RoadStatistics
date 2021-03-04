package by.roadstatistics.ui.settingsPart

import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.adapters.SettingsSpinnerAdapter
import by.roadstatistics.databinding.FragmentSettingsBinding
import by.roadstatistics.utils.Constants.BACK_STACK_FRAGMENT_TITLE
import by.roadstatistics.utils.Constants.CURRENT_POLYLINE_COLOR
import by.roadstatistics.utils.Constants.MAP_LOOP

// настройки

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        viewModelProvider = ViewModelProvider(this)
        binding.loopState.text = MAP_LOOP.toString()
        checkCurrentColor()

        binding.plusLoopSize.setOnClickListener {
            if (MAP_LOOP <= 19) {
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

        binding.settButtonBlack.setOnClickListener {
            binding.settButtonBlack.text = "ට"
            binding.settButtonYellow.text = null
            binding.settButtonGreen.text = null
            binding.settButtonRed.text = null
            CURRENT_POLYLINE_COLOR = R.color.black
        }

        binding.settButtonYellow.setOnClickListener {
            binding.settButtonBlack.text = null
            binding.settButtonYellow.text = "ට"
            binding.settButtonGreen.text = null
            binding.settButtonRed.text = null
            CURRENT_POLYLINE_COLOR = R.color.button_yellow
        }

        binding.settButtonGreen.setOnClickListener {
            binding.settButtonBlack.text = null
            binding.settButtonYellow.text = null
            binding.settButtonGreen.text = "ට"
            binding.settButtonRed.text = null
            CURRENT_POLYLINE_COLOR = R.color.button_green
        }

        binding.settButtonRed.setOnClickListener {
            binding.settButtonBlack.text = null
            binding.settButtonYellow.text = null
            binding.settButtonGreen.text = null
            binding.settButtonRed.text = "ට"
            CURRENT_POLYLINE_COLOR = R.color.button_red
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

    private fun checkCurrentColor() {
        when (CURRENT_POLYLINE_COLOR) {
            R.color.black -> binding.settButtonBlack.text = "ට"
            R.color.button_yellow -> binding.settButtonYellow.text = "ට"
            R.color.button_green -> binding.settButtonGreen.text = "ට"
            R.color.button_red -> binding.settButtonRed.text = "ට"
        }
    }

    override fun onPause() {
        BACK_STACK_FRAGMENT_TITLE = getString(R.string.fr_settings)
        super.onPause()
    }

}