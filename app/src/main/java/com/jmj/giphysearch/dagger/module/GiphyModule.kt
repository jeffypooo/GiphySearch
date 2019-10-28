package com.jmj.giphysearch.dagger.module

import com.google.gson.GsonBuilder
import com.jmj.giphysearch.BuildConfig
import com.jmj.giphysearch.domain.api.GiphyApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 *  GiphyModule.kt
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */

@Module
class GiphyModule(private val baseUrl: String = "https://api.giphy.com") {

  @Provides
  fun provideApi(): GiphyApi {
    val gson = GsonBuilder().setLenient().create()
    val rf = Retrofit.Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create(gson))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
      .build()
    return rf.create(GiphyApi::class.java)
  }

  @Provides
  @Named("giphyApiKey")
  fun provideApiKey(): String = BuildConfig.GIPHY_API_KEY

}