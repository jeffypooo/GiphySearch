package com.jmj.giphysearch.presentation.search

import com.google.gson.GsonBuilder
import com.jmj.giphysearch.domain.api.GiphyApi
import com.jmj.giphysearch.domain.api.model.SearchResponse
import com.jmj.giphysearch.domain.log.GlobalLogger
import com.jmj.giphysearch.domain.log.StdoutSink
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class SearchPresenterTest {

  private val mockApi = mock<GiphyApi> {
    on { search(any(), any(), any(), any(), any(), any()) } doReturn Observable.never()
  }
  private val mockApiKey = "mock_key"
  private val mockView = mock<SearchView> {}

  private val presenter by lazy {
    SearchPresenter(GlobalLogger, mockApi, mockApiKey)
  }

  @Before
  fun setup() {
    GlobalLogger.addSink(StdoutSink())
  }

  @After
  fun tearDown() {
    GlobalLogger.clearSinks()
  }


  @Test
  fun `on create, helper text is set to helpful message`() {
    presenter.onCreate(mockView)
    verify(mockView).setSearchHelperText("Tap search to find GIFs.")
  }

  @Test
  fun `on create, helper button shown`() {
    presenter.onCreate(mockView)
    verify(mockView).showSearchHelperButton(true)
  }

  @Test
  fun `on create, progress indicator is hidden`() {
    presenter.onCreate(mockView)
    verify(mockView).showProgressIndicator(false)
  }

  @Test
  fun `when search is clicked, helper text is set to empty`() {
    driveToResumed()

    /* user clicks search */
    presenter.onSearchMenuItemClick()

    verify(mockView).setSearchHelperText("")
  }

  @Test
  fun `when search is clicked, helper button is hidden`() {
    driveToResumed()

    /* user clicks search */
    presenter.onSearchMenuItemClick()

    verify(mockView).showSearchHelperButton(false)
  }

  @Test
  fun `when search is submitted, results are added from search api`() {
    /* stub api mock so we can control the response */
    val responseEmitter = PublishSubject.create<SearchResponse>()
    mockApi.stub {
      on { search(any(), any(), any(), any(), any(), any()) } doReturn responseEmitter
    }

    driveToResumed()

    /* user submits a search query */
    val query = "foobar"
    presenter.onSearchSubmitted(query)


    /* helper controls should display 'in progress' info to user here, and exisiting results should be cleared */
    verify(mockView).setSearchHelperText("Searching for 'foobar'...")
    verify(mockView).showProgressIndicator(true)
    verify(mockView).clearResults()

    /* api call is made */
    verify(mockApi).search(
      apiKey = mockApiKey,
      query = query,
      limit = 100,
      offset = 0
    )

    /* response comes back */
    val testResponse = getTestSearchResponse()
    responseEmitter.onNext(testResponse)

    /* results should be added and helper controls hidden */
    verify(mockView).setSearchHelperText("")
    verify(mockView, times(2)).showProgressIndicator(false)
    verify(mockView).addResults(testResponse.data)

  }

  @Test
  fun `when search is submitted, ui shows helper message if results is empty`() {
    /* stub api mock so we can control the response */
    val responseEmitter = PublishSubject.create<SearchResponse>()
    mockApi.stub {
      on { search(any(), any(), any(), any(), any(), any()) } doReturn responseEmitter
    }

    driveToResumed()

    /* user submits a search query */
    val query = "foobar"
    presenter.onSearchSubmitted(query)


    /* response comes back */
    val testResponse = getEmptyTestSearchResponse()
    responseEmitter.onNext(testResponse)

    /* results should be added and helper controls hidden */
    verify(mockView).setSearchHelperText("No results :(.")
    verify(mockView, times(2)).showProgressIndicator(false)
    verify(mockView).addResults(testResponse.data)

  }

  @Test
  fun `when search is submitted and unknown host error is thrown, ui shows helper message`() {
    /* stub mock for failure */
    mockApi.stub {
      on {
        search(any(), any(), any(), any(), any(), any())
      } doReturn Observable.error(UnknownHostException("stub"))
    }

    driveToResumed()

    /* user submits a search query */
    val query = "foobar"
    presenter.onSearchSubmitted(query)

    verify(mockView, times(2)).clearResults()
    verify(mockView).setSearchHelperText("Network error :(.")
    verify(mockView, times(2)).showProgressIndicator(false)
  }


  private fun driveToResumed() = presenter.run {
    onCreate(mockView)
    onStart()
    onResume()
  }

}

private fun getTestSearchResponse(): SearchResponse {
  val responseJson = """
      {
      "data": [
          {
              "type": "gif",
              "id": "ttQ5Jj9Lhn03e",
              "url": "https://giphy.com/gifs/xpost-burger-rburgers-ttQ5Jj9Lhn03e",
              "slug": "xpost-burger-rburgers-ttQ5Jj9Lhn03e",
              "bitly_gif_url": "https://gph.is/25HJMxt",
              "bitly_url": "https://gph.is/25HJMxt",
              "embed_url": "https://giphy.com/embed/ttQ5Jj9Lhn03e",
              "username": "",
              "source": "https://www.reddit.com/r/Cinemagraphs/comments/4cbj22/tombstone_burger_xpost_rburgers/",
              "title": "cheeseburgers GIF",
              "rating": "g",
              "content_url": "",
              "source_tld": "www.reddit.com",
              "source_post_url": "https://www.reddit.com/r/Cinemagraphs/comments/4cbj22/tombstone_burger_xpost_rburgers/",
              "is_sticker": 0,
              "import_datetime": "2016-04-05 01:42:03",
              "trending_datetime": "0000-00-00 00:00:00",
              "images": {
                  "fixed_height_still": {
                      "height": "200",
                      "size": "34516",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200_s.gif",
                      "width": "379"
                  },
                  "original_still": {
                      "height": "253",
                      "size": "87147",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy_s.gif",
                      "width": "480"
                  },
                  "fixed_width": {
                      "height": "105",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w.mp4",
                      "mp4_size": "13735",
                      "size": "29499",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w.webp",
                      "webp_size": "23482",
                      "width": "200"
                  },
                  "fixed_height_small_still": {
                      "height": "100",
                      "size": "13462",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100_s.gif",
                      "width": "190"
                  },
                  "fixed_height_downsampled": {
                      "height": "200",
                      "size": "59833",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200_d.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200_d.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200_d.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200_d.webp",
                      "webp_size": "44562",
                      "width": "379"
                  },
                  "preview": {
                      "height": "216",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy-preview.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy-preview.mp4",
                      "mp4_size": "27138",
                      "width": "411"
                  },
                  "fixed_height_small": {
                      "height": "100",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100.mp4",
                      "mp4_size": "12866",
                      "size": "30434",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100.webp",
                      "webp_size": "21410",
                      "width": "190"
                  },
                  "downsized_still": {
                      "height": "253",
                      "size": "87147",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy_s.gif",
                      "width": "480"
                  },
                  "downsized": {
                      "height": "253",
                      "size": "157787",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.gif",
                      "width": "480"
                  },
                  "downsized_large": {
                      "height": "253",
                      "size": "157787",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.gif",
                      "width": "480"
                  },
                  "fixed_width_small_still": {
                      "height": "53",
                      "size": "4608",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100w_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100w_s.gif",
                      "width": "100"
                  },
                  "preview_webp": {
                      "height": "210",
                      "size": "44218",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy-preview.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy-preview.webp",
                      "width": "398"
                  },
                  "fixed_width_still": {
                      "height": "105",
                      "size": "14567",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w_s.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w_s.gif",
                      "width": "200"
                  },
                  "fixed_width_small": {
                      "height": "53",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100w.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100w.mp4",
                      "mp4_size": "6226",
                      "size": "10782",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100w.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100w.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/100w.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=100w.webp",
                      "webp_size": "9632",
                      "width": "100"
                  },
                  "downsized_small": {
                      "height": "252",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy-downsized-small.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy-downsized-small.mp4",
                      "mp4_size": "67040",
                      "width": "480"
                  },
                  "fixed_width_downsampled": {
                      "height": "105",
                      "size": "18943",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w_d.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w_d.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200w_d.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200w_d.webp",
                      "webp_size": "17724",
                      "width": "200"
                  },
                  "downsized_medium": {
                      "height": "253",
                      "size": "157787",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.gif",
                      "width": "480"
                  },
                  "original": {
                      "frames": "14",
                      "hash": "956578e21e77e2ccd7410c76c98d9d39",
                      "height": "253",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.mp4",
                      "mp4_size": "57217",
                      "size": "157787",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.webp",
                      "webp_size": "81474",
                      "width": "480"
                  },
                  "fixed_height": {
                      "height": "200",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200.mp4",
                      "mp4_size": "34894",
                      "size": "82898",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200.gif",
                      "webp": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/200.webp?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=200.webp",
                      "webp_size": "51726",
                      "width": "379"
                  },
                  "looping": {
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy-loop.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy-loop.mp4",
                      "mp4_size": "640440"
                  },
                  "original_mp4": {
                      "height": "252",
                      "mp4": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy.mp4?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy.mp4",
                      "mp4_size": "57217",
                      "width": "480"
                  },
                  "preview_gif": {
                      "height": "122",
                      "size": "48923",
                      "url": "https://media1.giphy.com/media/ttQ5Jj9Lhn03e/giphy-preview.gif?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=giphy-preview.gif",
                      "width": "231"
                  },
                  "480w_still": {
                      "url": "https://media3.giphy.com/media/ttQ5Jj9Lhn03e/480w_s.jpg?cid=c821b5e76828fe58663855f358833fcde6fd0956ff66a3e9&rid=480w_s.jpg",
                      "width": "480",
                      "height": "253"
                  }
              },
              "analytics": {
                  "onload": {
                      "url": "https://giphy-analytics.giphy.com/simple_analytics?response_id=6828fe58663855f358833fcde6fd0956ff66a3e9&event_type=GIF_SEARCH&gif_id=ttQ5Jj9Lhn03e&action_type=SEEN"
                  },
                  "onclick": {
                      "url": "https://giphy-analytics.giphy.com/simple_analytics?response_id=6828fe58663855f358833fcde6fd0956ff66a3e9&event_type=GIF_SEARCH&gif_id=ttQ5Jj9Lhn03e&action_type=CLICK"
                  },
                  "onsent": {
                      "url": "https://giphy-analytics.giphy.com/simple_analytics?response_id=6828fe58663855f358833fcde6fd0956ff66a3e9&event_type=GIF_SEARCH&gif_id=ttQ5Jj9Lhn03e&action_type=SENT"
                  }
              }
          }
      ],
      "pagination": {
          "total_count": 1356,
          "count": 1,
          "offset": 0
      },
      "meta": {
          "status": 200,
          "msg": "OK",
          "response_id": "6828fe58663855f358833fcde6fd0956ff66a3e9"
      }
  }
    """
  return GsonBuilder()
    .setLenient()
    .create()
    .fromJson(responseJson, SearchResponse::class.java)
}

private fun getEmptyTestSearchResponse(): SearchResponse {
  val json = """
    {
      "data": [],
      "pagination": {
          "total_count": 0,
          "count": 0,
          "offset": 0
      },
      "meta": {
          "status": 200,
          "msg": "OK",
          "response_id": "27f2fce077989d15e5e6dcdc39db0fe5eb014a23"
      }
    }
  """
  return GsonBuilder()
    .setLenient()
    .create()
    .fromJson(json, SearchResponse::class.java)
}