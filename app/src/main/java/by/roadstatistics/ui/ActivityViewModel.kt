package by.roadstatistics.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.DatabaseRepository
import by.roadstatistics.utils.Constants.CURRENT_YEAR
import by.roadstatistics.utils.SelectedMonthMapper
import kotlinx.coroutines.*

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
    }

    fun actualMonth(month: String, context: Context) {
        getMonthIntMutableLiveData.value = SelectedMonthMapper(context).getMonthNumber(month)
    }

}