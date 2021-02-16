package by.roadstatistics.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.roadstatistics.database.CordInfo
import by.roadstatistics.databinding.ItemRecycleViewBinding

class DaysListAdapter : RecyclerView.Adapter<DaysListAdapter.DaysViewHolder>() {
     var daysList: List<CordInfo> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DaysViewHolder(
        ItemRecycleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) { holder.bind() }

    override fun getItemCount() = daysList.size

    class DaysViewHolder(private val binding: ItemRecycleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.recycleViewText.text = "example"
        }

    }

}