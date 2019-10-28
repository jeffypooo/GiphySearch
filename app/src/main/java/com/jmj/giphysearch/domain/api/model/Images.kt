package com.jmj.giphysearch.domain.api.model

import com.google.gson.annotations.SerializedName

data class Images(
  @SerializedName("fixed_height") val fixedHeight: GifImage,
  @SerializedName("fixed_height_still") val fixedHeightStill: StillImage,
  @SerializedName("fixed_width") val fixedWidth: GifImage,
  @SerializedName("fixed_width_still") val fixedWidthStill: StillImage,
  @SerializedName("fixed_width_downsampled") val fixedWidthDownsampled: GifImage,
  @SerializedName("downsized_large") val downsizedLarge: GifImage
)