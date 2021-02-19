package by.roadstatistics.ui.daysPart

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.roadstatistics.R
import by.roadstatistics.adapters.DaysListAdapter
import by.roadstatistics.database.CordInfo
import by.roadstatistics.databinding.FragmentDaysListBinding
import by.roadstatistics.ui.ActivityViewModel
import by.roadstatistics.ui.NavMainActivity

// спикос на основе ресайклера с днями и выбором месяца

class DaysListFragment : Fragment(R.layout.fragment_days_list) {

    private lateinit var binding: FragmentDaysListBinding
    private lateinit var localAdapter: DaysListAdapter
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding = FragmentDaysListBinding.bind(view)

        viewModelProvider = ViewModelProvider(requireActivity())
        viewModelProvider.get(ActivityViewModel::class.java).also {
            it.getMonthIntLiveData.observe(viewLifecycleOwner, { month ->

                Log.i("FFFF", month.toString())

            })

        }




        localAdapter = DaysListAdapter()
        binding.recyclerView.apply {
            adapter = localAdapter
            layoutManager = LinearLayoutManager(context)
        }

        localAdapter.daysList = listOf(CordInfo(123,123,123,123,123, 123F, 123F))


    }


}