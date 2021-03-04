package by.roadstatistics.utils

import android.view.textclassifier.TextClassifierEvent
import by.roadstatistics.R
import java.util.*

/**
 * Class to storage the constants. Some constants in the onPause method is saving to sharedPreference
 */

object Constants {

    @JvmStatic
    var BACK_STACK_FRAGMENT_TITLE = ""

    const val FRAGMENT_PICKET_DAY = 4

    const val BUNDLE_KEY_PICKET_DAY_FRAGMENT = "list of day data"

    @JvmStatic
    var CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR)

    @JvmStatic
    var CURRENT_MONTH = 0

    @JvmStatic
    var MAP_LOOP = 11.0F
    const val MAP_LOOP_KEY = "loop key"

    @JvmStatic
    var CURRENT_POLYLINE_COLOR = R.color.black
    const val COLOR_KEY = "color key"

    @JvmStatic
    var APP_START_COUNT = 1
    var APP_START_COUNT_KEY = "app start count key"

    const val FIREBASE_DATABASE_KEY = "Users"

    @JvmStatic
    var USER_ID = "0"
    const val USER_ID_KEY = "user id key"

}