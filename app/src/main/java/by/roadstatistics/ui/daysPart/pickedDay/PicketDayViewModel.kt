package by.roadstatistics.ui.daysPart.pickedDay

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.R
import com.google.android.gms.maps.model.LatLng
import java.util.*

class PicketDayViewModel : ViewModel() {

    private val cordAddressMutablyLiveData = MutableLiveData<String>()
    val startCordsAddressLiveData: LiveData<String> = cordAddressMutablyLiveData

    private val endCordsAddressMutablyLiveData = MutableLiveData<String>()
    val endCordsAddressLiveData: LiveData<String> = endCordsAddressMutablyLiveData

    fun getStartCordAddress(context: Context, lat: Double, lng: Double) {
        val address =
            Geocoder(context, Locale.getDefault()).getFromLocation(lat, lng, 1)[0].locality
        if (address != null) {
            cordAddressMutablyLiveData.value = address
        } else {
            cordAddressMutablyLiveData.value = context.getString(R.string.unknown_addres)
        }
    }

    fun getEndCordAddress(context: Context, lat: Double, lng: Double) {
        val address =
            Geocoder(context, Locale.getDefault()).getFromLocation(lat, lng, 1)[0].locality
        if (address != null) {
            cordAddressMutablyLiveData.value = address
        } else {
            cordAddressMutablyLiveData.value = context.getString(R.string.unknown_addres)
        }
    }

}