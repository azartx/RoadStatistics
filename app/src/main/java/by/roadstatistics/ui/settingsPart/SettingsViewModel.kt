package by.roadstatistics.ui.settingsPart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    private val mutable = MutableLiveData<String>()
    val text: LiveData<String> = mutable


}