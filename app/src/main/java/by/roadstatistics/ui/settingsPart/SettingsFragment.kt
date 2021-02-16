package by.roadstatistics.ui.settingsPart

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.adapters.SettingsSpinnerAdapter
import by.roadstatistics.adapters.SpinnerAdapter
import by.roadstatistics.database.DatabaseRepository
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

// настрйоки

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var databaseRepository: DatabaseRepository
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseRepository = DatabaseRepository(view.context)
        viewModelProvider = ViewModelProvider(this)

        viewModelProvider.get(SettingsViewModel::class.java).also { sett ->
            sett.yearListLiveData.observe(
                viewLifecycleOwner,
                { yearList ->
                    view.findViewById<Spinner>(R.id.settingsYearSpinner).apply {
                        adapter = SettingsSpinnerAdapter(view.context, R.id.dayOfWeek, yearList)
                    }

                })
            sett.getYearList(view.context)
        }


    }


}