package com.jmj.giphysearch.presentation.search.detail

import com.jmj.giphysearch.domain.api.GiphyApi
import com.jmj.giphysearch.domain.api.model.GetResponse
import com.jmj.giphysearch.domain.log.Logger
import com.jmj.giphysearch.presentation.common.AppFragmentPresenter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import javax.inject.Named

class GifDetailsPresenter @Inject constructor(
  private val logger: Logger,
  private val api: GiphyApi,
  @Named("giphyApiKey") private val giphyApiKey: String
) : AppFragmentPresenter<GifDetailsView>() {

  var gifId: String = ""

  private val gifDisposables = CompositeDisposable()

  override fun onViewCreated(view: GifDetailsView) {
    super.onViewCreated(view)
    loadGif()
  }

  fun onBrowserButtonClick(url: String) {
    logger.d(TAG, "opening '$url'")
    view?.openBrowser(url)
  }

  private fun loadGif() {
    logger.d(TAG, "loading $gifId...")
    api.getById(gifId, giphyApiKey)
      .subscribeBy(onError = this::gifFailedToLoad, onNext = this::gifLoaded)
      .addTo(gifDisposables)
  }

  private fun gifFailedToLoad(err: Throwable) {
    logger.e(TAG, "failed to load gif w/ $err")
  }

  private fun gifLoaded(res: GetResponse) {
    view?.apply {
      loadImage(res.data.images.downsizedLarge.url)
      setTitle(res.data.title)
      setUrl(res.data.bitlyUrl)
    }
  }

  companion object {
    private const val TAG = "GifDetailsPresenter"
  }

}