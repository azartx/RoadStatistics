package by.roadstatistics.ui.daysPart.pickedDay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PicketDayViewModel : ViewModel() {

    private val mutablyLiveData = MutableLiveData<List<Int>>()
    val liveData: LiveData<List<Int>> = mutablyLiveData

}