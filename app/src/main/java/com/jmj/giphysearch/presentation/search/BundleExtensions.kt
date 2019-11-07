package com.jmj.giphysearch.presentation.search

import android.os.Bundle

fun Bundle.putViewState(state: SearchView.State) {
  putString("helperText", state.helperText)
  putBoolean("isHelperButtonVisible", state.isHelperButtonVisible)
  putBoolean("isProgressIndicatorVisible", state.isProgressIndicatorVisible)
}

fun Bundle.readViewState() = SearchView.State(
  helperText = getString("helperText", ""),
  isHelperButtonVisible = getBoolean("isHelperButtonVisible"),
  isProgressIndicatorVisible = getBoolean("isProgressIndicatorVisible")
)