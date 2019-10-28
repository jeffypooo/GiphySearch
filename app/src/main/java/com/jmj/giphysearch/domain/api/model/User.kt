package com.jmj.giphysearch.domain.api.model

import com.google.gson.annotations.SerializedName

data class User(
  @SerializedName("avatar_url") val avatarUrl: String,
  @SerializedName("banner_url") val bannerUrl: String,
  @SerializedName("profile_url") val profileUrl: String,
  val username: String,
  @SerializedName("display_url") val displayName: String
)