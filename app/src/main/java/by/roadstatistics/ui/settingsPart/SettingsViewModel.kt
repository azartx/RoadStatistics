package by.roadstatistics.ui.settingsPart

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private lateinit var databaseRepository: DatabaseRepository

    private val yearListMutable = MutableLiveData<List<Int>>()
    var yearListLiveData: LiveData<List<Int>> = yearListMutable

    fun getYearList(context: Context) {
        databaseRepository = DatabaseRepository(context)
        CoroutineScope(Dispatchers.Main + Job()).launch {
            yearListMutable.value = databaseRepository.getYearList()
        }
    }


}