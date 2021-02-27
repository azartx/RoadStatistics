package by.roadstatistics.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CordInfo::class], version = 1)
abstract class DaysDB : RoomDatabase() {

    abstract fun getDaysDAO(): DaysDAO

    companion object {
        fun init(context: Context) =
            Room.databaseBuilder(context, DaysDB::class.java, "DaysRoadStatDB").build()
    }
}
