package by.roadstatistics.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import by.roadstatistics.R
import by.roadstatistics.utils.Constants.CURRENT_YEAR

class SettingsSpinnerAdapter(context: Context, resId: Int, var daysList: List<Int>) :
    ArrayAdapter<Int>(context, resId, daysList) {

    fun addAll(daysList: List<Int>) {
        this.daysList = daysList as MutableList<Int>
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

                val textView = this.findViewById<TextView>(R.id.dayOfWeek)

                    textView.text = daysList[position].toString()

                this.setOnClickListener {
                    val yearText: String = textView.text.toString()
                    CURRENT_YEAR = yearText.toInt()
                    Log.i("FFFF", CURRENT_YEAR.toString())
                }

            }
}