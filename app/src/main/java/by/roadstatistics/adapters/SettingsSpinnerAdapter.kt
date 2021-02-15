package by.roadstatistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import by.roadstatistics.R

class SettingsSpinnerAdapter(context: Context, resId: Int, var daysList: List<String>) :
    ArrayAdapter<String>(context, resId, daysList) {

    fun addAll(daysList: List<String>) {
        this.daysList = daysList as MutableList<String>
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup) =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spiner_row_settings, parent, false).apply {

                this.findViewById<TextView>(R.id.dayOfWeek).text = daysList[position]

            }
}