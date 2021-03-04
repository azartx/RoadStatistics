package by.roadstatistics.ui.mapPart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.utils.Constants.BACK_STACK_FRAGMENT_TITLE
import by.roadstatistics.utils.Constants.MAP_LOOP
import by.roadstatistics.utils.Constants.USER_ID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

/**
 * Logic of this fragment is only show places, where other users are.
 */

class MapGeneralFragment : Fragment(R.layout.fragment_map_root), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProvider = ViewModelProvider(this)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        localMap = googleMap
        createMarkers()
    }

    private fun createMarkers() {
        viewModelProvider.get(MapViewModel::class.java).also { vmp ->
            vmp.liveCordsLiveData.observe(viewLifecycleOwner, { users ->

                for (user in users) {
                    if (user.latDouble != null && user.lngDouble != null) {
                        if (user.id != USER_ID) {
                            MarkerOptions().apply {
                                title(user.name)
                                position(LatLng(user.latDouble, user.lngDouble))
                                localMap.addMarker(this)
                            }
                            // if id == USER_ID (location of app owner), then show his marker
                        } else {
                            MarkerOptions().apply {
                                title(user.name)
                                position(LatLng(user.latDouble, user.lngDouble))
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_me_marker))
                                localMap.addMarker(this)
                            }
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    user.latDouble,
                                    user.lngDouble
                                ), MAP_LOOP
                            ).apply {
                                localMap.moveCamera(this)
                            }
                        }
                    }
                }
            })
            vmp.getLiveCords()
        }
    }

    override fun onPause() {
        BACK_STACK_FRAGMENT_TITLE = getString(R.string.fr_map)
        super.onPause()
    }

}