package com.jmj.giphysearch.android.util

import android.app.SearchManager
import android.content.Intent

/**
 *  IntentExtensions.kt
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */

internal val Intent.isSearchAction: Boolean
  get() = action == Intent.ACTION_SEARCH

internal val Intent.searchQueryExtra: String?
  get() = getStringExtra(SearchManager.QUERY)