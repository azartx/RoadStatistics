package by.roadstatistics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.roadstatistics.databinding.ItemRecycleViewBinding

class DaysListAdapter : RecyclerView.Adapter<DaysListAdapter.DaysViewHolder>() {
     var daysList: List<Int> = emptyList()

    fun updateList(list: List<Int>) {
        daysList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaysViewHolder(
        ItemRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) { holder.bind(daysList[position]) }

    override fun getItemCount() = daysList.size

    class DaysViewHolder(private val binding: ItemRecycleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Int) {
            binding.recycleViewText.text = "Day $day"

        }

    }

}