package com.freeankit.freenotepad.view

import android.support.design.widget.BottomSheetDialogFragment
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.freeankit.freenotepad.R


/**
 * @author Ankit Kumar on 07/08/2018
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bottom_sheet, container, false)
    }
}