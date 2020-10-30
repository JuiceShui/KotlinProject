package com.demo.kotlin.view.widget.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ItemPaddingDecoration(left: Int, top: Int, right: Int, bottom: Int) :
    ItemDecoration() {
    private var mLeftOffset = -1
    private var mRightOffset = -1
    private var mTopOffset = -1
    private var mBottomOffset = -1
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (mLeftOffset > 0) {
            outRect.left = mLeftOffset
        }
        if (mTopOffset > 0) {
            outRect.top = mTopOffset
        }
        if (mRightOffset > 0) {
            outRect.right = mRightOffset
        }
        if (mBottomOffset > 0) {
            outRect.bottom = mBottomOffset
        }
    }

    init {
        mLeftOffset = left
        mTopOffset = top
        mRightOffset = right
        mBottomOffset = bottom
    }
}
