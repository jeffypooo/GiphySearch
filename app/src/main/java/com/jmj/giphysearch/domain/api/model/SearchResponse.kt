package com.jmj.giphysearch.domain.api.model

data class SearchResponse(
  val data: List<GifObject>,
  val pagination: Pagination,
  val meta: Meta
)