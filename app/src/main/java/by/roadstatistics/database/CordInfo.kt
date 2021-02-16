package by.roadstatistics.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CordInfo(
    @ColumnInfo val year: Int,
    @ColumnInfo val month: Int,
    @ColumnInfo val day: Int,
    @ColumnInfo val hours: Int,
    @ColumnInfo val minutes: Int,
    @ColumnInfo val latitude: Float,
    @ColumnInfo val longitude: Float
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0
}
