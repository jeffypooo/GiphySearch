package com.jmj.giphysearch.domain.api.model

import com.google.gson.annotations.SerializedName

data class Images(
  @SerializedName("fixed_height") val fixedHeight: GifImage,
  @SerializedName("fixed_height_still") val fixedHeightStill: StillImage,
  @SerializedName("fixed_width") val fixedWidth: GifImage,
  @SerializedName("fixed_width_still") val fixedWidthStill: StillImage,
  @SerializedName("downsized_still") val downsizedStill: StillImage
)