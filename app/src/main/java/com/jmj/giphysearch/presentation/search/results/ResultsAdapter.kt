package com.jmj.giphysearch.presentation.search.results

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.Target
import com.jmj.giphysearch.R
import com.jmj.giphysearch.android.util.layoutInflater
import com.jmj.giphysearch.domain.api.model.GifObject
import com.jmj.giphysearch.domain.log.GlobalLogger
import kotlinx.android.synthetic.main.rv_gif.view.*

class ResultsAdapter(private val glide: RequestManager) : RecyclerView.Adapter<GifViewHolder>() {

  private val gifs = mutableListOf<GifObject>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
    return GifViewHolder(
      layoutInflater(parent).inflate(
        R.layout.rv_gif,
        parent,
        false
      )
    )
  }

  override fun getItemCount(): Int {
    return gifs.size
  }

  override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
    val obj = gifs[position]
    glide.load(obj.images.fixedWidth.url)
      .override(Target.SIZE_ORIGINAL)
      .placeholder(R.drawable.ic_gif_placeholder)
      .centerCrop()
      .into(holder.imageView)
  }



  fun addAll(newGifs: Collection<GifObject>) {
    val insertionBegin = gifs.indices.last + 1
    val insertionCount = newGifs.size
    GlobalLogger.v(TAG, "adding $insertionCount items @ $insertionBegin")
    gifs.addAll(newGifs)
    notifyItemRangeInserted(insertionBegin, insertionCount)
  }

  fun clear() {
    val prevSize = gifs.size
    gifs.clear()
    notifyItemRangeRemoved(0, prevSize)
  }

  companion object {
    private const val TAG = "ResultsAdapter"
  }
}

class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  val imageView
    get() = itemView.image
}