package by.roadstatistics.utils.mappers

import android.content.Context
import by.roadstatistics.R

class SelectedMonthMapper(val context: Context) {
    fun getMonthNumber(month: String): Int = when (month) {
        context.getString(R.string.january) -> 1
        context.getString(R.string.february) -> 2
        context.getString(R.string.march) -> 3
        context.getString(R.string.april) -> 4
        context.getString(R.string.may) -> 5
        context.getString(R.string.june) -> 6
        context.getString(R.string.juli) -> 7
        context.getString(R.string.august) -> 8
        context.getString(R.string.september) -> 9
        context.getString(R.string.october) -> 10
        context.getString(R.string.november) -> 11
        context.getString(R.string.december) -> 12
        else -> 0
    }
}