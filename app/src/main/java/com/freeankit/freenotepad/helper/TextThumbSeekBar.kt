package com.freeankit.freenotepad.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.SeekBar
import com.freeankit.freenotepad.R


/**
 * @author Ankit Kumar on 31/07/2018
 */
class TextThumbSeekBar : SeekBar {
    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mThumbSize: Int = 0
    private var mTextPaint: TextPaint? = null

    init {
        mThumbSize = context.resources.getDimensionPixelSize(R.dimen.thumb_size)
        mTextPaint = TextPaint()
        mTextPaint?.color = context.resources.getColor(R.color.colorPrimary)
        mTextPaint?.textSize = context.resources.getDimensionPixelSize(R.dimen.thumb_text_size).toFloat()
        mTextPaint?.typeface = Typeface.DEFAULT_BOLD
        mTextPaint?.textAlign = Paint.Align.CENTER
    }

    @SuppressLint("DrawAllocation")
    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when {
            progress < 40 -> {
                mTextPaint?.color = context.resources.getColor(R.color.red)
//                canvas.draw(context.resources.getColor(R.color.red))
            }
            progress in 40..70 -> {
                mTextPaint?.color = context.resources.getColor(R.color.colorPrimary)
//                canvas.drawColor(context.resources.getColor(R.color.colorPrimary))
            }
            else -> {
                mTextPaint?.color = context.resources.getColor(R.color.green)
//                canvas.drawColor(context.resources.getColor(R.color.green))
            }
        }

        val progressText = (progress / 10).toString()
        val bounds = Rect()
        mTextPaint?.getTextBounds(progressText, 0, progressText.length, bounds)

        val leftPadding = paddingLeft - thumbOffset
        val rightPadding = paddingRight - thumbOffset
        val width = width - leftPadding - rightPadding
        val progressRatio = progress / max
        val thumbOffset = mThumbSize * (0.5f - progressRatio)
        val thumbX = progressRatio * width + leftPadding.toFloat() + thumbOffset
        val thumbY = height / 2f + bounds.height() / 2f
        canvas.drawText(progressText, 0, progressText.length, thumbX, thumbY, mTextPaint)
    }
}