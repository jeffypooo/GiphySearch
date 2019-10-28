package com.jmj.giphysearch.presentation.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jmj.giphysearch.R
import com.jmj.giphysearch.android.util.isSearchAction
import com.jmj.giphysearch.android.util.searchQueryExtra
import com.jmj.giphysearch.dagger.component.DaggerSearchComponent
import com.jmj.giphysearch.domain.api.model.GifObject
import com.jmj.giphysearch.domain.log.GlobalLogger
import com.jmj.giphysearch.presentation.common.AppActivity
import com.jmj.giphysearch.presentation.search.detail.GifDetailsBottomSheet
import com.jmj.giphysearch.presentation.search.results.PaddingDecoration
import com.jmj.giphysearch.presentation.search.results.ResultsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.SearchView as SearchViewWidget

class SearchActivity : AppActivity<SearchPresenter>(), SearchView {

  override val presenter: SearchPresenter by lazy {
    DaggerSearchComponent.create().searchPresenter()
  }

  private lateinit var menu: Menu

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContentView(R.layout.activity_main)

    /* setup the recycler view */
    //FIXME: this code is implicitly coupled to giphy API using 200 pixel fixed widths
    val numColumns = resources.displayMetrics.widthPixels / 200
    val layoutManager = StaggeredGridLayoutManager(numColumns, RecyclerView.VERTICAL)
    val adapter = ResultsAdapter(Glide.with(this)).apply {
      setOnItemClickListener(presenter::onGifClick)
    }
    gifRecyclerView.apply {
      this.adapter = adapter
      this.layoutManager = layoutManager
      addItemDecoration(PaddingDecoration(4f, 4f))
      addOnScrollListener(scrollListener)
    }

    /* other UI */
    searchHelperButton.setOnClickListener {
      // hacky but drives the same logic as a menu item click :)
      menu.performIdentifierAction(R.id.search, 0)
    }

    presenter.onCreate(this)

    handleIntent()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleIntent()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)

    /* connect the search menu item to the search manager so it generates the same
    * intents as the system search dialog. */
    val searchableInfo = getSystemService<SearchManager>()!!.getSearchableInfo(componentName)
    (menu.findItem(R.id.search).actionView as SearchViewWidget).apply {
      setSearchableInfo(searchableInfo)
      isIconifiedByDefault = false
    }

    this.menu = menu

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean = when {
    item.itemId == R.id.search -> {
      presenter.onSearchMenuItemClick()
      true
    }
    else                       -> false
  }

  override fun setSearchHelperText(status: String) = runOnUiThread {
    helperText.text = status
  }

  override fun showSearchHelperButton(show: Boolean) = runOnUiThread {
    searchHelperButton.isVisible = show
  }

  override fun showProgressIndicator(show: Boolean) = runOnUiThread {
    progressIndicator.isVisible = show
  }

  override fun addResults(results: List<GifObject>) = runOnUiThread {
    resultsAdapter.addAll(results)
  }

  override fun clearResults() = runOnUiThread {
    resultsAdapter.clear()
  }

  override fun presentGifDetailsSheet(id: String) = runOnUiThread {
    GifDetailsBottomSheet.newInstance(id).show(supportFragmentManager, "gif_dets_$id")
  }

  override fun shortSnackbar(msg: String) = runOnUiThread {
    Snackbar.make(contentView, msg, Snackbar.LENGTH_SHORT).show()
  }

  override fun longSnackbar(msg: String) = runOnUiThread {
    Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).show()
  }

  override fun dismiss() = runOnUiThread { finish() }

  private fun handleIntent() {
    if (intent.isSearchAction) presenter.onSearchSubmitted(intent.searchQueryExtra!!)
  }

  private val resultsAdapter
    get() = gifRecyclerView.adapter as ResultsAdapter

  private val scrollListener = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      if (dy < 0) return // ignore if scrolling up
      val lm = recyclerView.layoutManager as StaggeredGridLayoutManager
      /* if we have reached the bottom, the position (index) of the first
      * visible item PLUS the visible item count should be equal to total item count. */
      val visible = lm.childCount
      val total = lm.itemCount
      val firstVisiblePositions = lm.findFirstVisibleItemPositions(null) ?: intArrayOf()
      val fistVisiblePosition = firstVisiblePositions[0]
      if (visible + fistVisiblePosition >= total) presenter.onScrolledToBottom()

    }
  }


  companion object {
    private const val TAG = "MainActivity"
  }
}

