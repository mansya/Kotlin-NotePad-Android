package com.freeankit.freenotepad.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.freeankit.freenotepad.R
import com.freeankit.freenotepad.view.adapters.ColorAdapter
import kotlinx.android.synthetic.main.bottom_sheet.*


/**
 * @author Ankit Kumar on 07/08/2018
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colors = context?.resources?.getIntArray(R.array.androidcolors)
        recycler_color.layoutManager = GridLayoutManager(context, 5)
        recycler_color.adapter = context?.let { colors?.let { it1 -> ColorAdapter(it, it1) } }
    }
}