package by.roadstatistics.ui.daysPart.pickedDay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.roadstatistics.R
import by.roadstatistics.database.CordInfo
import by.roadstatistics.utils.Constants.BUNDLE_KEY_PICKET_DAY_FRAGMENT

class PicketDayFragment : Fragment(R.layout.fragment_picket_day) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dayInfoList = arguments?.getParcelableArrayList<CordInfo>(BUNDLE_KEY_PICKET_DAY_FRAGMENT)



    }

}