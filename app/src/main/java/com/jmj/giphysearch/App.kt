package com.jmj.giphysearch

import android.app.Application
import com.jmj.giphysearch.android.log.LogcatSink
import com.jmj.giphysearch.domain.log.GlobalLogger


/**
 * Application class for GiphySearch.
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    /* setup logging */
    GlobalLogger.addSink(LogcatSink())
    GlobalLogger.i(TAG, "app launched.")

  }

  companion object {
    private const val TAG = "App"
  }

}