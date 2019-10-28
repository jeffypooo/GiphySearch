package com.jmj.giphysearch.domain.api.model

import com.google.gson.annotations.SerializedName

data class GifObject(
  val type: String,
  val id: String,
  val slug: String,
  val url: String,
  @SerializedName("bitly_url") val bitlyUrl: String,
  @SerializedName("embed_url") val embedUrl: String,
  val username: String,
  val source: String,
  val rating: String,
  @SerializedName("content_url") val contentUrl: String,
  val user: User,
  @SerializedName("source_tld") val sourceTopLevelDomain: String,
  @SerializedName("source_post_url") val sourcePostUrl: String,
  @SerializedName("update_datetime") val updateTime: String,
  @SerializedName("create_datetime") val createTime: String,
  @SerializedName("import_datetime") val importTime: String,
  @SerializedName("trending_datetime") val trendingTime: String,
  val images: Images,
  val title: String
)