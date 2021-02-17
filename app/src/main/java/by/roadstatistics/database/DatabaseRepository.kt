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

}