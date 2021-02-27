package by.roadstatistics.ui.mapPart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.roadstatistics.R
import by.roadstatistics.database.firebase.User
import by.roadstatistics.utils.Constants.USER_ID
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// главная карта

class MapGeneralFragment : Fragment(R.layout.fragment_map_root), OnMapReadyCallback {

    private lateinit var localMap: GoogleMap
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelProvider = ViewModelProvider(this)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)




//db.push().setValue(User("0", "Jack", "123.2", "41243.2"))


    }

    override fun onMapReady(googleMap: GoogleMap) {
        localMap = googleMap

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
                        } else {
                            //TODO("NEED TO SET SOME MARKER FOR OWNER USER")
                        }
                }
                }

            })
            vmp.getLiveCords()
        }

    }

}