package by.roadstatistics.ui.daysPart.pickedDay

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.roadstatistics.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale

class PicketDayViewModel : ViewModel() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private val cordAddressMutablyLiveData = MutableLiveData<String>()
    val startCordsAddressLiveData: LiveData<String> = cordAddressMutablyLiveData

    private val endCordsAddressMutablyLiveData = MutableLiveData<String>()
    val endCordsAddressLiveData: LiveData<String> = endCordsAddressMutablyLiveData

    fun getStartCordAddress(context: Context, lat: Double, lng: Double) {
        coroutineScope.launch {
            cordAddressMutablyLiveData.value = withContext(Dispatchers.IO) {
                val geoCoder = try {
                    Geocoder(context, Locale.getDefault()).getFromLocation(lat, lng, 1)[0]
                } catch (e: Exception) {
                    null
                }

                return@withContext try {
                    "${geoCoder?.countryCode ?: ""} ${geoCoder?.locality ?: ""} ${geoCoder?.subLocality ?: ""} ${geoCoder?.featureName ?: ""}"
                } catch (e: Exception) {
                    "Load address is failed. Turn ON internet or reboot app"
                }
            } ?: context.getString(R.string.unknown_addres)
        }
    }

    fun getEndCordAddress(context: Context, lat: Double, lng: Double) {
        coroutineScope.launch {
            endCordsAddressMutablyLiveData.value = withContext(Dispatchers.IO) {
                val geoCoder = try {
                    Geocoder(context, Locale.getDefault()).getFromLocation(lat, lng, 1)[0]
                } catch (e: Exception) {
                    null
                }

                return@withContext try {
                    "${geoCoder?.countryCode ?: ""} ${geoCoder?.locality ?: ""} ${geoCoder?.subLocality ?: ""} ${geoCoder?.featureName ?: ""}"
                } catch (e: Exception) {
                    "Load address is failed. Turn ON internet or reboot app"
                }
            } ?: context.getString(R.string.unknown_addres)
        }
    }
}