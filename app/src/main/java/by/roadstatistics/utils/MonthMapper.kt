package by.roadstatistics.utils

import android.content.Context
import by.roadstatistics.R

class MonthMapper(var list: List<Int>, var context: Context) {

    fun monthsToString(): List<String> {
        val newList = mutableListOf<String>()
        for (month in list) {
            newList.add(map(month))
        }
        return newList
    }

    private fun map(month: Int): String = when (month) {
        1 -> context.getString(R.string.january)
        2 -> context.getString(R.string.february)
        3 -> context.getString(R.string.march)
        4 -> context.getString(R.string.april)
        5 -> context.getString(R.string.may)
        6 -> context.getString(R.string.june)
        7 -> context.getString(R.string.juli)
        8 -> context.getString(R.string.august)
        9 -> context.getString(R.string.september)
        10 -> context.getString(R.string.october)
        11 -> context.getString(R.string.november)
        12 -> context.getString(R.string.december)
        else -> context.getString(R.string.no_months)
    }

}