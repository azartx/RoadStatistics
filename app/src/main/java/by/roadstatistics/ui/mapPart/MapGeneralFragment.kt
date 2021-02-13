package by.roadstatistics.ui.mapPart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

// главная карта

class MapGeneralFragment : Fragment(R.layout.fragment_map_root), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        localMap = googleMap


    }

}