package com.aleksandrgenrikhs.pokemon.presentation

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class BottomOffsetDecoration(private val offset: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == state.itemCount - 1) {
            outRect.set(0, 0, 0, offset)
        } else {
            outRect.setEmpty()
        }
    }
}