package com.freeankit.freenotepad

import android.content.Context
import android.view.LayoutInflater

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 21/12/2017 (MM/DD/YYYY )
 */
val Context.layoutInflator get() = LayoutInflater.from(this)