package com.jmj.giphysearch.domain.api

import com.jmj.giphysearch.domain.api.model.SearchResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *  GiphyApi
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */


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


}


