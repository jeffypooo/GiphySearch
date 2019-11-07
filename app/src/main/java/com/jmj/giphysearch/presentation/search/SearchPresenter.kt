package com.jmj.giphysearch.presentation.search

import com.jmj.giphysearch.domain.api.GiphyApi
import com.jmj.giphysearch.domain.api.model.GifObject
import com.jmj.giphysearch.domain.api.model.SearchResponse
import com.jmj.giphysearch.domain.log.Logger
import com.jmj.giphysearch.presentation.common.AppActivityPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.min

class SearchPresenter @Inject constructor(
  private val logger: Logger,
  private val giphyApi: GiphyApi,
  @Named("giphyApiKey") private val giphyApiKey: String
) : AppActivityPresenter<SearchView>() {

  private val searchDisposables = CompositeDisposable()
  private val searchState = SearchState()

  var viewState = SearchView.State(
    helperText = "Tap search to find GIFs.",
    isHelperButtonVisible = true
  )

  override fun onCreate(view: SearchView) {
    super.onCreate(view)
    logger.d(TAG, "created")
    view.applyState(viewState)
  }

  override fun onDestroy() {
    super.onDestroy()
    logger.d(TAG, "destroyed")
    searchDisposables.clear()
  }

  fun onSearchMenuItemClick() {
    updateViewState(helperText = "", isHelperButtonVisible = false)
  }

  fun onSearchSubmitted(query: String) {
    searchDisposables.clear()
    searchState.reset(query)
    view?.clearResults()
    updateViewState(helperText = "Searching for '$query'...")
    doSearch()
  }

  fun onScrolledToBottom() {
    if (searchState.isRunningQuery) return
    val newOffset = min(searchState.offset + SEARCH_PAGE_SIZE, searchState.total)
    if (newOffset < searchState.total) {
      logger.d(TAG, "new offset = $newOffset")
      searchState.offset = newOffset
      doSearch()
    } else {
      logger.d(TAG, "end of results.")
    }
  }

  fun onGifClick(obj: GifObject) {
    logger.d(TAG, "presenting dets for ${obj.id}")
    view?.presentGifDetailsSheet(obj.id)
  }

  private fun doSearch() {
    logger.d(TAG, "searching for '${searchState.query}'...")
    searchState.isRunningQuery = true
    updateViewState(isProgressIndicatorVisible = true)
    giphyApi.search(giphyApiKey, searchState.query, SEARCH_PAGE_SIZE, searchState.offset)
      .subscribeBy(onError = this::searchFailed, onNext = this::searchComplete)
      .addTo(searchDisposables)
  }

  private fun searchFailed(err: Throwable) {
    logger.e(TAG, err, "search failed")
    searchState.reset()
    view?.clearResults()
    updateViewState(
      helperText = if (err is UnknownHostException) "Network error :(." else "Unknown error :(.",
      isProgressIndicatorVisible = false
    )

  }

  private fun searchComplete(res: SearchResponse) {
    searchState.apply {
      isRunningQuery = false
      total = res.pagination.totalCount
    }
    updateViewState(
      helperText = if (res.data.isEmpty()) "No results :(." else "",
      isProgressIndicatorVisible = false
    )
    logger.d(TAG, "adding ${res.pagination.count} results to view")
    view?.addResults(res.data)
  }

  private fun updateViewState(
    helperText: String = viewState.helperText,
    isHelperButtonVisible: Boolean = viewState.isHelperButtonVisible,
    isProgressIndicatorVisible: Boolean = viewState.isProgressIndicatorVisible
  ) {
    viewState = viewState.copy(
      helperText = helperText,
      isHelperButtonVisible = isHelperButtonVisible,
      isProgressIndicatorVisible = isProgressIndicatorVisible
    ).also { logger.v(TAG, "view state = $it") }
    view?.applyState(viewState)
  }


  private class SearchState(
    var query: String = "",
    var offset: Int = 0,
    var total: Int = 0,
    var isRunningQuery: Boolean = false
  ) {
    fun reset(newQuery: String = "") {
      query = newQuery
      offset = 0
      total = 0
      isRunningQuery = false
    }
  }

  companion object {
    private const val TAG = "SearchPresenter"
    const val SEARCH_PAGE_SIZE = 20
  }


}

