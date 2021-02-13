package by.roadstatistics.ui.daysPart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.roadstatistics.R
import by.roadstatistics.adapters.DaysListAdapter
import by.roadstatistics.database.Day
import by.roadstatistics.databinding.FragmentDaysListBinding

// спикос на основе ресайклера с днями и выбором месяца

class DaysListFragment : Fragment(R.layout.fragment_days_list) {

    private lateinit var binding: FragmentDaysListBinding
    private lateinit var localAdapter: DaysListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            binding = FragmentDaysListBinding.bind(view)


        localAdapter = DaysListAdapter()
        binding.recyclerView.apply {
            adapter = localAdapter
            layoutManager = LinearLayoutManager(context)
        }

        localAdapter.daysList = listOf(Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2), Day(123,123,123F,123.2,123.2))


    }


}