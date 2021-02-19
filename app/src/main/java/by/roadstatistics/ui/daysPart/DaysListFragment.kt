package by.roadstatistics.ui.daysPart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.roadstatistics.R
import by.roadstatistics.adapters.DaysListAdapter
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
                        daysViewModel.dayLiveData.observe(viewLifecycleOwner, { daysOfMonthList ->
                            Log.i("FFFF", daysOfMonthList.size.toString() + "qweqweqweqwe")
                            if (::localAdapter.isInitialized) {
                                localAdapter.updateList(daysOfMonthList)
                            } else {
                                localAdapter = DaysListAdapter()
                                binding.recyclerView.apply {
                                    adapter = localAdapter
                                    layoutManager = LinearLayoutManager(context)
                                    localAdapter.updateList(daysOfMonthList)
                                    Log.i("FFFF", localAdapter.daysList.size.toString())
                                }
                            }



                        })
                        daysViewModel.getDaysInMonth(view.context, CURRENT_MONTH, CURRENT_YEAR)
                    }

                }
            })

        }








    }


}