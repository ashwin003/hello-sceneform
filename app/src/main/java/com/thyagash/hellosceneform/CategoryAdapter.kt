package com.thyagash.hellosceneform

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CategoryAdapter(context: Context, val data: ArrayList<Category>) : ArrayAdapter<Category>(context, R.layout.spinner_template, data) {

    override fun getItem(position: Int): Category? {
        return data[position]
    }

    override fun getPosition(item: Category?): Int {
        if(item == null) {
            return 0
        }
        return data.indexOf(item!!)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dataModel = getItem(position) as Category

        var inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.spinner_template, parent, false)

        val nameView = rowView.findViewById<TextView>(R.id.categoryText)

        nameView.text = dataModel.text

        return rowView
    }
}