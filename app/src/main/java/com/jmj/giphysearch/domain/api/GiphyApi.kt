package com.jmj.giphysearch.domain.api

import com.jmj.giphysearch.domain.api.model.GetResponse
import com.jmj.giphysearch.domain.api.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GiphyApi {

  @GET("v1/gifs/search")
  fun search(
    @Query("api_key") apiKey: String,
    @Query("q") query: String,
    @Query("limit") limit: Int,
    @Query("offset") offset: Int = 0,
    @Query("rating") rating: String = "R",
    @Query("lang") lang: String = "en"
  ): Observable<SearchResponse>

  @GET("v1/gifs/{gif_id}")
  fun getById(
    @Path("gif_id") gifId: String,
    @Query("api_key") apiKey: String
  ): Observable<GetResponse>


}


