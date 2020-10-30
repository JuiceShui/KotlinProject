package com.demo.kotlin.view.widget.adapter.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ItemSpaceDecoration(private val space: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
//		outRect.left = space;
//		outRect.right = space;
//		outRect.bottom = space;
//		if (parent.getChildAdapterPosition(view) == 0) {
        outRect.top = space
        //		}
    }
}