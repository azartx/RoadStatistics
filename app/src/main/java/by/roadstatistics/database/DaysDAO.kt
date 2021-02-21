package by.roadstatistics.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaysDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCordInfoToDatabase(cordInfo: CordInfo)

    @Query("SELECT DISTINCT year FROM CordInfo")
    fun getYearList(): List<Int>

    // забираем все месяца без копий и возвращаем списком
    @Query("SELECT DISTINCT month FROM CordInfo WHERE year = :year")
    fun getYearMonths(year: Int): List<Int>

    @Query("SELECT * FROM CordInfo WHERE year = :year AND month = :month")
    fun getMonthDaysInfo(month: Int, year: Int): List<CordInfo>

    @Query("SELECT DISTINCT day FROM CordInfo WHERE month = :month AND year = :year")
    fun getDaysInMonth(month: Int, year: Int): List<Int>

    @Query("SELECT * FROM CordInfo Where year = :year AND month = :month AND day = :day")
    fun getDay(year: Int, month: Int, day: Int): List<CordInfo>

}