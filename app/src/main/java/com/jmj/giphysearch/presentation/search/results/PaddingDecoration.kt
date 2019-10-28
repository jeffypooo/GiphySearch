package com.jmj.giphysearch.presentation.search.results

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class PaddingDecoration(
  private val verticalPad: Float,
  private val horizontalPad: Float
) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    with(horizontalPad.roundToInt()) {
      outRect.left = this
      outRect.right = this
    }
    with(verticalPad.roundToInt()) {
      outRect.top = this
      outRect.bottom = this
    }
  }
}