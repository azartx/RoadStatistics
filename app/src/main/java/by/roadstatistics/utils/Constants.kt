package by.roadstatistics.utils

import java.util.*

object Constants {

    const val FRAGMENT_DAYS_LIST = 1
    const val FRAGMENT_MAP_GENERAL = 2
    const val FRAGMENT_SETTINGS = 3
    const val FRAGMENT_PICKET_DAY = 4

    const val BUNDLE_KEY_PICKET_DAY_FRAGMENT = "list of day data"

    @JvmStatic
    var CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)

    @JvmStatic
    var CURRENT_MONTH = 0

    @JvmStatic
    var MAP_LOOP = 11.0F
    const val MAP_LOOP_KEY = "loop key"

}