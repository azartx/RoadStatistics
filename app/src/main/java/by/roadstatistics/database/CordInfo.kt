package by.roadstatistics.database

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable  {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeInt(hours)
        parcel.writeInt(minutes)
        parcel.writeFloat(latitude)
        parcel.writeFloat(longitude)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CordInfo> {
        override fun createFromParcel(parcel: Parcel): CordInfo {
            return CordInfo(parcel)
        }

        override fun newArray(size: Int): Array<CordInfo?> {
            return arrayOfNulls(size)
        }
    }
}
