package by.roadstatistics.ui.daysPart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.roadstatistics.R
import by.roadstatistics.adapters.DaysListAdapter
import by.roadstatistics.database.CordInfo
import by.roadstatistics.databinding.FragmentDaysListBinding
import by.roadstatistics.ui.ActivityViewModel
import by.roadstatistics.utils.Constants.CURRENT_MONTH
import by.roadstatistics.utils.Constants.CURRENT_YEAR

// спикос на основе ресайклера с днями и выбором месяца

class DaysListFragment : Fragment(R.layout.fragment_days_list) {

    private lateinit var binding: FragmentDaysListBinding
    private lateinit var localAdapter: DaysListAdapter
    private lateinit var activityViewModelProvider: ViewModelProvider
    private lateinit var thisViewModelProvider: ViewModelProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDaysListBinding.bind(view)
        activityViewModelProvider = ViewModelProvider(requireActivity())
        thisViewModelProvider = ViewModelProvider(this)

        activityViewModelProvider.get(ActivityViewModel::class.java).also {
            it.getMonthIntLiveData.observe(viewLifecycleOwner, { month ->
                if (month != CURRENT_MONTH) {
                    Log.i("FFFF", month.toString())
                    CURRENT_MONTH = month

                    thisViewModelProvider.get(DaysViewModel::class.java).also { daysViewModel ->
                        daysViewModel.daysLiveData.observe(viewLifecycleOwner, {
                            Log.i("FFFF", it[0].latitude.toString())
                        })
                        daysViewModel.getMonthDaysInfo(view.context, CURRENT_MONTH, CURRENT_YEAR)
                    }

                }
            })

        }




        localAdapter = DaysListAdapter()
        binding.recyclerView.apply {
            adapter = localAdapter
            layoutManager = LinearLayoutManager(context)
        }

        localAdapter.daysList = listOf(CordInfo(123, 123, 123, 123, 123, 123F, 123F))


    }


}