package com.thyagash.hellosceneform

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ModelAdapter(context: Context, val data: ArrayList<Model>) : ArrayAdapter<Model>(context, R.layout.icon_template, data) {
    private lateinit var selectedModel: Model

    override fun getItem(position: Int): Model? {
        return data[position]
    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup): View {
        val dataModel = getItem(position) as Model

        var inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.icon_template, parent, false)

        val nameView = rowView.findViewById<TextView>(R.id.name)
        val iconView = rowView.findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.icon)

        nameView.text = dataModel.name
        iconView.setImageResource(dataModel.icon)

        return rowView
    }

    fun getSelectedModel(): Model = selectedModel
}