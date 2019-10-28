package com.jmj.giphysearch.presentation.search

import com.jmj.giphysearch.domain.api.GiphyApi
import com.jmj.giphysearch.domain.api.model.SearchResponse
import com.jmj.giphysearch.domain.log.Logger
import com.jmj.giphysearch.presentation.common.AppActivityPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Named

class SearchPresenter @Inject constructor(
  private val logger: Logger,
  private val giphyApi: GiphyApi,
  @Named("giphyApiKey") private val giphyApiKey: String
) : AppActivityPresenter<SearchView>() {

  private val searchDisposables = CompositeDisposable()
  //FIXME: these should probably be protected by a lock/monitor
  private var currentSearchQuery = ""
  private var currentOffset = 0
  private var searchQueryRunning = false

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
    currentSearchQuery = query
    currentOffset = 0
    view?.apply {
      clearResults()
      setSearchHelperText("Searching for '$query'...")
    }
    doSearch()
  }

  fun onScrolledToBottom() {
    if (searchQueryRunning) return
    currentOffset += SEARCH_PAGE_SIZE
    doSearch()
  }

  private fun doSearch() {
    logger.d(TAG, "searching for '$currentSearchQuery'...")
    view?.showProgressIndicator(true)
    searchQueryRunning = true
    giphyApi.search(giphyApiKey, currentSearchQuery, limit = SEARCH_PAGE_SIZE, offset = currentOffset)
      .subscribeBy(onError = this::searchFailed, onNext = this::searchComplete)
      .addTo(searchDisposables)
  }

  private fun searchFailed(err: Throwable) {
    logger.e(TAG, "search failed - $err")
    searchQueryRunning = false
    view?.apply {
      clearResults()
      showProgressIndicator(false)
      setSearchHelperText(if (err is UnknownHostException) "Network error :(." else "Unknown error :(.")
    }

  }

  private fun searchComplete(res: SearchResponse) {
    dumpResponse(res)
    searchQueryRunning = false
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
    const val SEARCH_PAGE_SIZE = 50
  }
}