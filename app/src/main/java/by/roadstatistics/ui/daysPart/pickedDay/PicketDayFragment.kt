package by.roadstatistics.ui.daysPart.pickedDay

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.utils.Constants.BUNDLE_KEY_PICKET_DAY_FRAGMENT
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
        localMap = googleMap


        //localMap.addMarker(MarkerOptions().title("Hello world").snippet("Additional text").position(LatLng(dayInfoList[0].longitude.toDouble(), dayInfoList[0].longitude.toDouble())))


        PolylineOptions().also { polylineOptions ->
            for (marker in dayInfoList) {
                polylineOptions.add(LatLng(marker.latitude.toDouble(), marker.longitude.toDouble()))
            }
            localMap.addPolyline(polylineOptions)
        }



    }

}