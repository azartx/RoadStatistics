package by.roadstatistics.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.database.DatabaseRepository
import by.roadstatistics.utils.Constants.CURRENT_YEAR
import by.roadstatistics.utils.SelectedMonthMapper
import com.google.firebase.database.*
import kotlinx.coroutines.*

class ActivityViewModel : ViewModel() {

    private lateinit var databaseRepository: DatabaseRepository
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private val mainScope = CoroutineScope(Dispatchers.Main + Job())

    private val monthMutableLiveData = MutableLiveData<List<Int>>()
    var monthListLiveData: LiveData<List<Int>> = monthMutableLiveData

    private val getMonthIntMutableLiveData = MutableLiveData<Int>()
    var getMonthIntLiveData: LiveData<Int> = getMonthIntMutableLiveData

    private val mutableLiveData = MutableLiveData<String>()
    var idLiveData: LiveData<String> = mutableLiveData

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

    fun getNextId() {



    }

}