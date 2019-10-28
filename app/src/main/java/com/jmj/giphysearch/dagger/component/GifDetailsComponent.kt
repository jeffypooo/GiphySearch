package com.jmj.giphysearch.dagger.component

import com.jmj.giphysearch.dagger.module.GiphyModule
import com.jmj.giphysearch.dagger.module.LoggingModule
import com.jmj.giphysearch.presentation.search.detail.GifDetailsPresenter
import dagger.Component

@Component(modules = [LoggingModule::class, GiphyModule::class])
interface GifDetailsComponent {

  fun presenter(): GifDetailsPresenter

}