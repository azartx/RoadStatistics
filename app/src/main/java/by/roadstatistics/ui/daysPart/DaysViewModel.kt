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

    private val daysInfoMutablyLiveData = MutableLiveData<List<CordInfo>>()
    val daysInfoLiveData: LiveData<List<CordInfo>> = daysInfoMutablyLiveData

    fun getDaysInMonth(context: Context, month: Int, year: Int) {
        databaseRepository = DatabaseRepository(context)
        coroutineScope.launch {
            dayMutablyLiveData.value = databaseRepository.getDaysInMonth(month, year)
        }

    }

    fun getMonthDaysInfo(context: Context, month: Int, year: Int) {
        databaseRepository = DatabaseRepository(context)
        coroutineScope.launch {
            daysInfoMutablyLiveData.value = databaseRepository.getMonthDaysInfo(month, year)
        }

    }


}