package com.jmj.giphysearch.presentation.search

import com.jmj.giphysearch.domain.api.model.GifObject
import com.jmj.giphysearch.presentation.common.AppView


interface SearchView : AppView {
  fun addResults(results: List<GifObject>)
  fun clearResults()
  fun setSearchHelperText(status: String)
  fun showSearchHelperButton(show: Boolean)
  fun showProgressIndicator(show: Boolean)
}