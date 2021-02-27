package by.roadstatistics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import by.roadstatistics.R

class SpinnerAdapter(
    context: Context,
    resId: Int,
    var daysList: List<String>
) :
    ArrayAdapter<String>(context, resId, daysList), AdapterView.OnItemSelectedListener {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_spiner_row, parent, false)
        val textView = view.findViewById<TextView>(R.id.dayOfWeek)
        textView.text = daysList[position]
        return view
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}



