package by.roadstatistics.ui.daysPart.pickedDay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.databinding.FragmentPicketDayBinding
import by.roadstatistics.utils.Constants.BUNDLE_KEY_PICKET_DAY_FRAGMENT
import by.roadstatistics.utils.Constants.CURRENT_POLYLINE_COLOR
import by.roadstatistics.utils.Constants.MAP_LOOP
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

/**
 * This fragment show to user info about the selected day.
 */

class PicketDayFragment : Fragment(R.layout.fragment_picket_day), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap
    private lateinit var dayInfoList: ArrayList<CordInfo>
    private lateinit var picketDayViewModelProvider: ViewModelProvider
    private lateinit var binding: FragmentPicketDayBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPicketDayBinding.bind(view)
        picketDayViewModelProvider = ViewModelProvider(this)

        dayInfoList = arguments?.getParcelableArrayList(BUNDLE_KEY_PICKET_DAY_FRAGMENT)
            ?: arrayListOf()

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        initMap(googleMap) // set base map settings
        createStartEndMarkers()
        createPolyline()
    }

    private fun initMap(googleMap: GoogleMap) {
        googleMap.also { map ->
            val cameraLocation = CameraUpdateFactory
                .newLatLngZoom(
                    LatLng(
                        dayInfoList[0].latitude.toDouble(),
                        dayInfoList[0].longitude.toDouble()
                    ),
                    MAP_LOOP
                )
            map.moveCamera(cameraLocation)
            localMap = map
        }
    }

    private fun createStartEndMarkers() {
            picketDayViewModelProvider.get(PicketDayViewModel::class.java).also { vm ->
                vm.startCordsAddressLiveData.observe(viewLifecycleOwner, { address ->
                    makeStartMarker(address)
                })
                vm.endCordsAddressLiveData.observe(viewLifecycleOwner, { address ->
                    makeEndMarker(address)
                })
                vm.distanceLiveData.observe(viewLifecycleOwner, { distance ->
                    binding.text1.text = distance.plus(getString(R.string.km_distance))
                })
                vm.getStartCordAddress(
                    requireContext(),
                    dayInfoList[0].latitude.toDouble(),
                    dayInfoList[0].longitude.toDouble()
                )
                vm.getEndCordAddress(
                    requireContext(),
                    dayInfoList[dayInfoList.size - 1].latitude.toDouble(),
                    dayInfoList[dayInfoList.size - 1].longitude.toDouble()
                )
                vm.getDistance(dayInfoList)
        }
    }

    private fun makeStartMarker(address: String) {
        MarkerOptions().apply {
            title(getString(R.string.startWalk))
            snippet(address)
            position(
                LatLng(
                    dayInfoList[0].latitude.toDouble(),
                    dayInfoList[0].longitude.toDouble()
                )
            )
            localMap.addMarker(this)
        }
    }

    private fun makeEndMarker(address: String) {
        MarkerOptions().apply {
            title(getString(R.string.endWalk))
            snippet(address)
            position(
                LatLng(
                    dayInfoList[dayInfoList.size - 1].latitude.toDouble(),
                    dayInfoList[dayInfoList.size - 1].longitude.toDouble()
                )
            )
            localMap.addMarker(this)
        }
    }

    /**
     * this method create polyline using "for" loop. This loop connects lines to each other.
     */
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
                    lat = marker.latitude.toDouble()
                    lng = marker.longitude.toDouble()
                }
            }
            polylineOptions.color(resources.getColor(CURRENT_POLYLINE_COLOR, null ))
            localMap.addPolyline(polylineOptions)
        }
    }

}