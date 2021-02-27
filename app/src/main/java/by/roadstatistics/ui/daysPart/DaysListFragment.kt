package by.roadstatistics.ui.daysPart

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.roadstatistics.R
import by.roadstatistics.adapters.DaysListAdapter
import by.roadstatistics.databinding.FragmentDaysListBinding
import by.roadstatistics.ui.ActivityViewModel
import by.roadstatistics.ui.NavMainActivity
import by.roadstatistics.utils.AdapterToFragmentListener
import by.roadstatistics.utils.Constants.BUNDLE_KEY_PICKET_DAY_FRAGMENT
import by.roadstatistics.utils.Constants.CURRENT_MONTH
import by.roadstatistics.utils.Constants.CURRENT_YEAR
import by.roadstatistics.utils.Constants.FRAGMENT_PICKET_DAY
import java.util.ArrayList

class DaysListFragment : Fragment(R.layout.fragment_days_list) {

    private lateinit var binding: FragmentDaysListBinding
    private lateinit var localAdapter: DaysListAdapter
    private lateinit var activityViewModelProvider: ViewModelProvider
    private lateinit var thisViewModelProvider: ViewModelProvider
    private lateinit var adapterToFragmentListener: AdapterToFragmentListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDaysListBinding.bind(view)
        activityViewModelProvider = ViewModelProvider(requireActivity())
        thisViewModelProvider = ViewModelProvider(this)

        activityViewModelProvider.get(ActivityViewModel::class.java).also {
            it.getMonthIntLiveData.observe(viewLifecycleOwner, { month ->
                CURRENT_MONTH = month
                thisViewModelProvider.get(DaysViewModel::class.java).also { daysViewModel ->
                    pickSomeDay(daysViewModel, view.context)
                    daysViewModel.dayLiveData.observe(viewLifecycleOwner, { daysOfMonthList ->
                        createRecyclerViewAdapter(daysOfMonthList)
                    })
                    daysViewModel.getDaysInMonth(view.context, CURRENT_MONTH, CURRENT_YEAR)
                }
            })
        }
    }

    private fun createRecyclerViewAdapter(daysOfMonthList: List<Int>) {
        localAdapter = DaysListAdapter(adapterToFragmentListener)
        binding.recyclerView.apply {
            adapter = localAdapter
            layoutManager = LinearLayoutManager(context)
            localAdapter.updateList(daysOfMonthList)
        }
    }

    private fun pickSomeDay(daysViewModel: DaysViewModel, context: Context) {
        adapterToFragmentListener = object : AdapterToFragmentListener {
            override fun onDeyNumberSend(dayNumber: Int) {
                daysViewModel.dayInfoLiveData.observe(viewLifecycleOwner, { dayInfoList ->
                    Bundle().also { bundle ->
                        bundle.putParcelableArrayList(
                            BUNDLE_KEY_PICKET_DAY_FRAGMENT,
                            dayInfoList as ArrayList<out Parcelable>
                        )
                        (activity as NavMainActivity).onFragmentChange(FRAGMENT_PICKET_DAY, bundle)
                    }
                })
                daysViewModel.getDay(context, CURRENT_YEAR, CURRENT_MONTH, dayNumber)
            }
        }
    }

}