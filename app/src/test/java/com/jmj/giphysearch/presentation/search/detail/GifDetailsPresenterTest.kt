package com.jmj.giphysearch.presentation.search.detail

import com.google.gson.GsonBuilder
import com.jmj.giphysearch.domain.api.GiphyApi
import com.jmj.giphysearch.domain.api.model.GetResponse
import com.jmj.giphysearch.domain.log.GlobalLogger
import com.jmj.giphysearch.domain.log.StdoutSink
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 *  GifDetailsPresenterTest
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */
class GifDetailsPresenterTest {

  private val mockApi = mock<GiphyApi> {
    on { getById(any(), any()) } doReturn Observable.never()
  }
  private val mockView = mock<GifDetailsView> {}
  private val fakeApiKey = "fake_api_key"

  private val presenter by lazy { GifDetailsPresenter(GlobalLogger, mockApi, fakeApiKey) }

  @Before
  fun setup() {
    GlobalLogger.addSink(StdoutSink())
  }

  @After
  fun tearDown() {
    GlobalLogger.clearSinks()
  }

  @Test
  fun `when view is created, gif is loaded from api and shown`() {
    /* stub api mock to return test resp */
    val testResponse = getTestGetReponse()
    mockApi.stub {
      on { getById(any(), any()) } doReturn Observable.just(testResponse)
    }
    presenter.gifId = "12345"
    presenter.onViewCreated(mockView)

    verify(mockApi).getById("12345", fakeApiKey)

    /* view should be populated */
    verify(mockView).loadImage(testResponse.data.images.downsizedLarge.url)
    verify(mockView).setTitle(testResponse.data.title)
    verify(mockView).setUrl(testResponse.data.bitlyUrl)
  }

  @Test
  fun `when open browser button is clicked, browser is opened`() {
    presenter.run {
      gifId = "12345"
      onViewCreated(mockView)
      onStart()
      onResume()
    }

    /* user clicks browser button */
    val url = "https://www.foo.com"
    presenter.onBrowserButtonClick(url)

    /* browser is opened */
    verify(mockView).openBrowser(url)

  }

}

private fun getTestGetReponse(): GetResponse {
  val json = """{
    "data": {
        "type": "gif",
        "id": "N9UnrUkeATLPy",
        "url": "https://giphy.com/gifs/N9UnrUkeATLPy",
        "slug": "N9UnrUkeATLPy",
        "bitly_gif_url": "https://gph.is/29fB2Jz",
        "bitly_url": "https://gph.is/29fB2Jz",
        "embed_url": "https://giphy.com/embed/N9UnrUkeATLPy",
        "username": "",
        "source": "https://imgur.com/gallery/G5ad2mz",
        "title": "test bed GIF",
        "rating": "g",
        "content_url": "",
        "source_tld": "imgur.com",
        "source_post_url": "https://imgur.com/gallery/G5ad2mz",
        "is_sticker": 0,
        "import_datetime": "2016-07-01 04:27:25",
        "trending_datetime": "1970-01-01 00:00:00",
        "images": {
            "downsized_large": {
                "height": "187",
                "size": "5850757",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy.gif",
                "width": "333"
            },
            "fixed_height_small_still": {
                "height": "100",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/100_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100_s.gif",
                "width": "178"
            },
            "original": {
                "frames": "176",
                "height": "187",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy.mp4",
                "mp4_size": "169603",
                "size": "5850757",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy.webp",
                "webp_size": "1600676",
                "width": "333"
            },
            "fixed_height_downsampled": {
                "height": "200",
                "size": "237390",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200_d.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200_d.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/200_d.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200_d.webp",
                "webp_size": "56594",
                "width": "356"
            },
            "downsized_still": {
                "height": "140",
                "size": "23217",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-downsized_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-downsized_s.gif",
                "width": "250"
            },
            "fixed_height_still": {
                "height": "200",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200_s.gif",
                "width": "356"
            },
            "downsized_medium": {
                "height": "187",
                "size": "2816922",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-downsized-medium.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-downsized-medium.gif",
                "width": "333"
            },
            "downsized": {
                "height": "140",
                "size": "1165810",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-downsized.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-downsized.gif",
                "width": "250"
            },
            "fixed_height_small": {
                "height": "100",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/100.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100.mp4",
                "mp4_size": "49434",
                "size": "1620646",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/100.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/100.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100.webp",
                "webp_size": "619716",
                "width": "178"
            },
            "original_mp4": {
                "height": "268",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy.mp4",
                "mp4_size": "169603",
                "width": "480"
            },
            "fixed_width_small": {
                "height": "56",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/100w.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100w.mp4",
                "mp4_size": "24580",
                "size": "538082",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/100w.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100w.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/100w.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100w.webp",
                "webp_size": "292586",
                "width": "100"
            },
            "downsized_small": {
                "height": "186",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-downsized-small.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-downsized-small.mp4",
                "mp4_size": "113423",
                "width": "332"
            },
            "preview": {
                "height": "186",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-preview.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-preview.mp4",
                "mp4_size": "35658",
                "width": "332"
            },
            "fixed_width_downsampled": {
                "height": "112",
                "size": "80058",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w_d.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w_d.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w_d.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w_d.webp",
                "webp_size": "23692",
                "width": "200"
            },
            "fixed_width_small_still": {
                "height": "56",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/100w_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=100w_s.gif",
                "width": "100"
            },
            "fixed_height": {
                "height": "200",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/200.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200.mp4",
                "mp4_size": "117217",
                "size": "6336129",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/200.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200.webp",
                "webp_size": "1662546",
                "width": "356"
            },
            "original_still": {
                "height": "187",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy_s.gif",
                "width": "333"
            },
            "fixed_width_still": {
                "height": "112",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w_s.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w_s.gif",
                "width": "200"
            },
            "looping": {
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-loop.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-loop.mp4",
                "mp4_size": "412768"
            },
            "fixed_width": {
                "height": "112",
                "mp4": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w.mp4?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w.mp4",
                "mp4_size": "54210",
                "size": "1999615",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w.gif",
                "webp": "https://media0.giphy.com/media/N9UnrUkeATLPy/200w.webp?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=200w.webp",
                "webp_size": "691374",
                "width": "200"
            },
            "preview_gif": {
                "height": "72",
                "size": "48003",
                "url": "https://media0.giphy.com/media/N9UnrUkeATLPy/giphy-preview.gif?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=giphy-preview.gif",
                "width": "128"
            },
            "480w_still": {
                "url": "https://media2.giphy.com/media/N9UnrUkeATLPy/480w_s.jpg?cid=c821b5e73d83d8f73d8aa767d49d8f40302773c7a0bb0fc0&rid=480w_s.jpg",
                "width": "480",
                "height": "270"
            }
        }
    },
    "meta": {
        "status": 200,
        "msg": "OK",
        "response_id": "3d83d8f73d8aa767d49d8f40302773c7a0bb0fc0"
    }
  }""".trimIndent()
  return GsonBuilder().setLenient().create().fromJson(json, GetResponse::class.java)

}