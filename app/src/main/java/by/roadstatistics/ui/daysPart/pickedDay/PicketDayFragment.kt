package by.roadstatistics.ui.daysPart.pickedDay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.utils.Constants.BUNDLE_KEY_PICKET_DAY_FRAGMENT
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class PicketDayFragment : Fragment(R.layout.fragment_picket_day), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap
    private lateinit var dayInfoList: ArrayList<CordInfo>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dayInfoList = arguments?.getParcelableArrayList<CordInfo>(BUNDLE_KEY_PICKET_DAY_FRAGMENT)
            ?: arrayListOf()

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.also { map ->
            val cameraLocation = CameraUpdateFactory
                .newLatLngZoom(
                    LatLng(
                        dayInfoList[0].latitude.toDouble(),
                        dayInfoList[0].longitude.toDouble()
                    ),
                    9.0f
                )
            map.moveCamera(cameraLocation)
            localMap = map
        }

        // в переменные записываются координаты, а в добавлении полилайна проверяется,
        // не добавлялись ли такое координаты в прошлом ходе, что бы не было одинаковых корд в одной точке

        makeStartMarker()
        createPolyline()
        makeEndMarker()


    }

    private fun makeEndMarker() {
        MarkerOptions().apply {
            title("End")
            snippet("End cord is here")
            position(
                LatLng(
                    dayInfoList[dayInfoList.size - 1].latitude.toDouble(),
                    dayInfoList[dayInfoList.size - 1].longitude.toDouble()
                )
            )

            localMap.addMarker(this)
        }
    }

    private fun createPolyline() {
        PolylineOptions().also { polylineOptions ->
            var lat = 0.0
            var lng = 0.0
            for (marker in dayInfoList) {
                if (marker.latitude.toDouble() != lat && marker.longitude.toDouble() != lng) {
                    polylineOptions.add(
                        LatLng(
                            marker.latitude.toDouble(),
                            marker.longitude.toDouble()
                        )
                    )
                    Log.i("FFFF", marker.latitude.toDouble().toString() + " lat")
                    Log.i("FFFF", marker.longitude.toDouble().toString() + " lon")
                    lat = marker.latitude.toDouble()
                    lng = marker.longitude.toDouble()
                }
            }
            localMap.addPolyline(polylineOptions)
        }
    }

    private fun makeStartMarker() {
        MarkerOptions().apply {
            title("Start")
            snippet("Start cord is here")
            position(
                LatLng(
                    dayInfoList[0].latitude.toDouble(),
                    dayInfoList[0].longitude.toDouble()
                )
            )

            localMap.addMarker(this)
        }
    }

}