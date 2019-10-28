package com.jmj.giphysearch.domain.api.model

import com.google.gson.annotations.SerializedName

data class GifImage(
  val url: String,
  val width: String,
  val height: String,
  val size: String,
  val mp4: String,
  @SerializedName("mp4_size") val mp4Size: String,
  val webp: String,
  @SerializedName("webp_size") val webpSize: String
)