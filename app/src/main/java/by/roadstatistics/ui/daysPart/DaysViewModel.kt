package by.roadstatistics.ui.daysPart

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.CordInfo
import by.roadstatistics.database.DatabaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DaysViewModel : ViewModel() {

    private lateinit var databaseRepository: DatabaseRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private val dayMutablyLiveData = MutableLiveData<List<Int>>()
    val dayLiveData: LiveData<List<Int>> = dayMutablyLiveData

    private val dayInfoMutablyLiveData = MutableLiveData<List<CordInfo>>()
    val dayInfoLiveData: LiveData<List<CordInfo>> = dayInfoMutablyLiveData

    fun getDaysInMonth(context: Context, month: Int, year: Int) {
        databaseRepository = DatabaseRepository(context)
        coroutineScope.launch {
            dayMutablyLiveData.value = databaseRepository.getDaysInMonth(month, year)
            databaseRepository.closeDatabase()
        }
    }

    fun getDay(context: Context, year: Int, month: Int, day: Int) {
        databaseRepository = DatabaseRepository(context)
        coroutineScope.launch {
            dayInfoMutablyLiveData.value = databaseRepository.getDay(year, month, day)
            databaseRepository.closeDatabase()
        }
    }

}