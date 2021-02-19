package by.roadstatistics.database

import android.content.Context
import kotlinx.coroutines.*

class DatabaseRepository(context: Context) {

    private val mainScope = CoroutineScope(Dispatchers.Main + Job())
    private val database = DaysDB.init(context)
    private val threadIO = Dispatchers.IO

    fun addCordsToDatabase(cordField: CordInfo) {
        mainScope.launch {
            withContext(threadIO) {
                database.getDaysDAO().addCordInfoToDatabase(cordField)
            }
        }
    }

    suspend fun getYearList(): List<Int> {
        return withContext(threadIO) {
            return@withContext database.getDaysDAO().getYearList()
        }
    }

    suspend fun getMonthList(currentYear: Int): List<Int> {
        return withContext(threadIO) {
            return@withContext database.getDaysDAO().getYearMonths(currentYear)
        }
    }

    suspend fun getMonthDaysInfo(month: Int, year: Int): List<CordInfo> {
        return withContext(threadIO) {
            return@withContext database.getDaysDAO().getMonthDaysInfo(month, year)
        }
    }

    suspend fun getDaysInMonth(month: Int, year: Int): List<Int> {
        return withContext(threadIO) {
            return@withContext database.getDaysDAO().getDaysInMonth(month, year)
        }
    }

}