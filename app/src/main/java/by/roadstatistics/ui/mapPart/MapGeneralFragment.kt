package by.roadstatistics.ui.mapPart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import by.roadstatistics.database.firebase.User
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

// главная карта

class MapGeneralFragment : Fragment(R.layout.fragment_map_root), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val db = FirebaseDatabase.getInstance().getReference("Users")


db.push().setValue(User("0", "Jack", "123.2", "41243.2"))


    }

    override fun onMapReady(googleMap: GoogleMap) {
        localMap = googleMap


    }

}