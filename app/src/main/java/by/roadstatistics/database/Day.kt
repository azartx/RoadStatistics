package by.roadstatistics.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Day(
    @ColumnInfo private val year: Int,
    @ColumnInfo private val month: Int,
    @ColumnInfo private val totalTravel: Float,
    @ColumnInfo private val startCords: Double,
    @ColumnInfo private val endCords: Double
) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private val id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
        parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeFloat(totalTravel)
        parcel.writeDouble(startCords)
        parcel.writeDouble(endCords)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Day> {
        override fun createFromParcel(parcel: Parcel): Day {
            return Day(parcel)
        }

        override fun newArray(size: Int): Array<Day?> {
            return arrayOfNulls(size)
        }
    }
}
