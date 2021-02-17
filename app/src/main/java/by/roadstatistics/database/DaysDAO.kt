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

}