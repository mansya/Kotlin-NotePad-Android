package com.freeankit.freenotepad.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.helper.layoutInflator
import kotlinx.android.synthetic.main.bottom_sheet_item.view.*

/**
 * @author Ankit Kumar on 07/08/2018
 */
class ColorAdapter(val context: Context, private val colors: IntArray) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val view = context.layoutInflator.inflate(R.layout.bottom_sheet_item, parent, false)
        return ColorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.itemView.selected_color.setBackgroundColor(colors[position])
    }

    inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}