package com.jmj.giphysearch.presentation.search.detail

import com.jmj.giphysearch.presentation.common.AppView

interface GifDetailsView : AppView {

  fun loadImage(url: String)
  fun setTitle(title: String)
  fun setUrl(url: String)
  fun openBrowser(url: String)
}