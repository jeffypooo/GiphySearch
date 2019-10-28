package com.jmj.giphysearch.android.util

import android.app.SearchManager
import android.content.Intent

internal val Intent.isSearchAction: Boolean
  get() = action == Intent.ACTION_SEARCH

internal val Intent.searchQueryExtra: String?
  get() = getStringExtra(SearchManager.QUERY)