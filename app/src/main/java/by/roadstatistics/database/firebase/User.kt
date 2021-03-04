package by.roadstatistics.database.firebase

/**
 * base user class.
 *
 * @property latDouble is convert constructor value lat to Double
 * @property lngDouble is convert constructor value lng to Double
 */

data class User(var id: String, var name: String, var lat: String?, var lng: String?) {
    val latDouble = lat?.toDouble()
    val lngDouble = lng?.toDouble()
}