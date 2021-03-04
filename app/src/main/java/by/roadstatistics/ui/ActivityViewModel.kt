package by.roadstatistics.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.DatabaseRepository
import by.roadstatistics.utils.Constants.CURRENT_YEAR
import by.roadstatistics.utils.mappers.SelectedMonthMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {

    private lateinit var databaseRepository: DatabaseRepository
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())

    private val monthMutableLiveData = MutableLiveData<List<Int>>()
    var monthListLiveData: LiveData<List<Int>> = monthMutableLiveData

    private val getMonthIntMutableLiveData = MutableLiveData<Int>()
    var getMonthIntLiveData: LiveData<Int> = getMonthIntMutableLiveData

    fun getMonthList(context: Context) {
        databaseRepository = DatabaseRepository(context)
        mainScope.launch {
            monthMutableLiveData.value = databaseRepository.getMonthList(CURRENT_YEAR)
        }
        databaseRepository.closeDatabase()
    }

    fun actualMonth(month: String, context: Context) {
        getMonthIntMutableLiveData.value = SelectedMonthMapper(context).getMonthNumber(month)
        databaseRepository.closeDatabase()
    }

}