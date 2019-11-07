package com.jmj.giphysearch.presentation.search.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.jmj.giphysearch.R
import com.jmj.giphysearch.dagger.component.DaggerGifDetailsComponent
import com.jmj.giphysearch.presentation.common.AppBottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_gif_details.*


class GifDetailsBottomSheet : AppBottomSheetDialogFragment<GifDetailsPresenter>(), GifDetailsView {

  override val presenter: GifDetailsPresenter by lazy {
    DaggerGifDetailsComponent.create().presenter()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_gif_details, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.gifId = arguments!!.getString(ARG_GIF_ID, "")
    presenter.onViewCreated(this)
  }

  override fun loadImage(url: String) = activity!!.runOnUiThread {
    Glide.with(this)
      .load(url)
      .placeholder(R.drawable.ic_gif_placeholder)
      .override(Target.SIZE_ORIGINAL)
      .centerCrop()
      .into(image)
  }

  override fun setTitle(title: String) = activity!!.runOnUiThread {
    this.title.text = title
  }

  override fun setUrl(url: String) = activity!!.runOnUiThread {
    urlText.text = url
    browserButton.setOnClickListener { presenter.onBrowserButtonClick(url) }
  }

  override fun openBrowser(url: String) = activity!!.runOnUiThread {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
  }

  override fun shortSnackbar(msg: String) = activity!!.runOnUiThread {
    Snackbar.make(contentView, msg, Snackbar.LENGTH_SHORT).show()
  }

  override fun longSnackbar(msg: String) = activity!!.runOnUiThread {
    Snackbar.make(contentView, msg, Snackbar.LENGTH_LONG).show()
  }

  companion object {
    private const val TAG = "GifDetailsBottomSheet"
    private const val ARG_GIF_ID = "gif.id"

    fun newInstance(gifId: String): GifDetailsBottomSheet {
      val args = Bundle().apply { putString(ARG_GIF_ID, gifId) }
      return GifDetailsBottomSheet().apply { arguments = args }
    }
  }

}