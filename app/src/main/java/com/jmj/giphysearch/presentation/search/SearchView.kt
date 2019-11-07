package com.jmj.giphysearch.presentation.search

import com.jmj.giphysearch.domain.api.model.GifObject
import com.jmj.giphysearch.presentation.common.AppView


interface SearchView : AppView {
  fun addResults(results: List<GifObject>)
  fun clearResults()
  fun applyState(state: State)
  fun presentGifDetailsSheet(id: String)

  data class State(
    val helperText: String = "",
    val isHelperButtonVisible: Boolean = true,
    val isProgressIndicatorVisible: Boolean = false
  )

}