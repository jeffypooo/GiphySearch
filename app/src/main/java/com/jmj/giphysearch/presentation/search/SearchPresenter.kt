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

  override fun onCreate(view: SearchView) {
    super.onCreate(view)
    view.apply {
      setSearchHelperText("Tap search to find GIFs.")
      showSearchHelperButton(true)
      showProgressIndicator(false)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    searchDisposables.clear()
  }

  fun onSearchMenuItemClick() {
    view?.apply {
      setSearchHelperText("")
      showSearchHelperButton(false)
    }
  }

  fun onSearchSubmitted(query: String) {
    searchDisposables.clear()
    searchState.reset()
    searchState.query = query
    view?.apply {
      clearResults()
      setSearchHelperText("Searching for '$query'...")
    }
    doSearch()
  }

  fun onScrolledToBottom() {
    if (searchState.isRunningQuery) return
    val newOffset = min(
      searchState.offset + SEARCH_PAGE_SIZE,
      searchState.total
    )
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
    view?.showProgressIndicator(true)
    giphyApi.search(giphyApiKey, searchState.query, SEARCH_PAGE_SIZE, searchState.offset)
      .subscribeBy(onError = this::searchFailed, onNext = this::searchComplete)
      .addTo(searchDisposables)
  }

  private fun searchFailed(err: Throwable) {
    logger.e(TAG, err, "search failed")
    searchState.reset()
    view?.apply {
      clearResults()
      showProgressIndicator(false)
      setSearchHelperText(if (err is UnknownHostException) "Network error :(." else "Unknown error :(.")
    }

  }

  private fun searchComplete(res: SearchResponse) {
    dumpResponse(res)
    searchState.isRunningQuery = false
    searchState.total = res.pagination.totalCount
    logger.d(TAG, "adding ${res.pagination.count} results to view")
    view?.apply {
      setSearchHelperText(if (res.data.isEmpty()) "No results :(." else "")
      showProgressIndicator(false)
      addResults(res.data)
    }
  }

  private fun dumpResponse(res: SearchResponse) {
    val metaDesc = res.meta.let { "${it.status} ${it.msg}" }
    val paginationDesc = res.pagination.let {
      "count: ${it.count}, offs: ${it.offset}, total: ${it.totalCount}"
    }
    logger.v(TAG, "search response - $metaDesc - $paginationDesc")
  }

  companion object {
    private const val TAG = "SearchPresenter"
    const val SEARCH_PAGE_SIZE = 20
  }
}

class SearchState(
  var query: String = "",
  var offset: Int = 0,
  var total: Int = 0,
  var isRunningQuery: Boolean = false
) {
  fun reset() {
    query = ""
    offset = 0
    total = 0
    isRunningQuery = false
  }
}