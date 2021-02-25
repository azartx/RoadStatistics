package by.roadstatistics.utils

import android.location.Location
import by.roadstatistics.database.CordInfo

class CordsToKmMapper {
    fun latLonToDistance(list: ArrayList<CordInfo>): Int {
        val f: FloatArray = floatArrayOf(1F)
        var distance = 0
        for ((index, value) in list.withIndex()) {
            if (index != list.size - 1) {
                Location.distanceBetween(
                    list[index].latitude.toDouble(),
                    list[index].longitude.toDouble(),
                    list[index + 1].latitude.toDouble(),
                    list[index + 1].longitude.toDouble(),
                    f
                )
                distance += f[0].toInt()
            }
        }
        return distance
    }
}