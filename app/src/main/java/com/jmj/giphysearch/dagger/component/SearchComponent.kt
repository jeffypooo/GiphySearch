package com.jmj.giphysearch.dagger.component

import com.jmj.giphysearch.dagger.module.GiphyModule
import com.jmj.giphysearch.dagger.module.LoggingModule
import com.jmj.giphysearch.presentation.search.SearchPresenter
import dagger.Component

@Component(modules = [LoggingModule::class, GiphyModule::class])
interface SearchComponent {

  fun searchPresenter(): SearchPresenter

}