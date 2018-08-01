package com.freeankit.freenotepad.helper

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 1/8/18 (MM/DD/YYYY)
 */
class CustomSeekbar(context: Context, attributes: AttributeSet) : SeekBar(context, attributes) {
    private var mThumb: Drawable? = null
    override fun setThumb(thumb: Drawable?) {
        super.setThumb(thumb)
        mThumb = thumb

    }

    fun getSeekbarThumb(): Drawable? {
        return mThumb

    }
}