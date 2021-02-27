package by.roadstatistics.database.firebase

data class User(var id: String, var name: String, var lat: String?, var lng: String?) {

    val latDouble = lat?.toDouble()
    val lngDouble = lng?.toDouble()

}