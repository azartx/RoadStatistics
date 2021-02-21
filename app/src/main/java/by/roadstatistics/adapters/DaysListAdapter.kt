package by.roadstatistics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.roadstatistics.databinding.ItemRecycleViewBinding
import by.roadstatistics.ui.NavMainActivity
import by.roadstatistics.utils.AdapterToFragmentListener
import by.roadstatistics.utils.ChangeFragmentListener

class DaysListAdapter(var adapterToFragmentListener: AdapterToFragmentListener) : RecyclerView.Adapter<DaysListAdapter.DaysViewHolder>() {
     var daysList: List<Int> = emptyList()

    fun updateList(list: List<Int>) {
        daysList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaysViewHolder(
        ItemRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) { holder.bind(daysList[position], adapterToFragmentListener) }

    override fun getItemCount() = daysList.size

    class DaysViewHolder(private val binding: ItemRecycleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int, adapterToFragmentListener: AdapterToFragmentListener) {
            binding.recycleViewText.text = "Day $day"

            binding.root.setOnClickListener {
                adapterToFragmentListener.onDeyNumberSend(dayNumber = day)
            }

        }

    }



}