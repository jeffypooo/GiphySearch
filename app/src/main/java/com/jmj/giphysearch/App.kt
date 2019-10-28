package com.jmj.giphysearch

import android.app.Application
import com.jmj.giphysearch.android.log.LogcatSink
import com.jmj.giphysearch.domain.log.GlobalLogger

/**
 *  App.kt
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */


/**
 * Application class for GiphySearch.
 */
class App : Application() {

  override fun onCreate() {
    super.onCreate()
    /* setup logging */
    GlobalLogger.addSink(LogcatSink())
    GlobalLogger.i(TAG, "app launched.")
//        GiphyCoreUI.configure(this, BuildConfig.GIPHY_API_KEY)

  }

  companion object {
    private const val TAG = "App"
  }

}