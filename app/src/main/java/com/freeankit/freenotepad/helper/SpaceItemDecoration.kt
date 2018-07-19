package com.freeankit.freenotepad.helper

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author Ankit Kumar (ankitdroiddeveloper@gmail.com) on 20/12/2017 (MM/DD/YYYY )
 */
class SpaceItemDecoration(context: Context, dimens: Int) : RecyclerView.ItemDecoration() {
    var space: Int = context.resources.getDimensionPixelOffset(dimens)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        when (getOrientation(parent)) {
            LinearLayoutManager.VERTICAL -> if (position != 0) outRect.top = space
            LinearLayoutManager.HORIZONTAL -> if (position != 0) outRect.left = space
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        val lm = parent.layoutManager
        return (lm as? LinearLayoutManager)?.orientation ?: -1
    }
}