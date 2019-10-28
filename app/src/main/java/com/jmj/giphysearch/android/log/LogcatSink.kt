package com.jmj.giphysearch.android.log

import android.annotation.SuppressLint
import android.util.Log
import com.jmj.giphysearch.domain.log.LogLevel
import com.jmj.giphysearch.domain.log.LogSink

/**
 *  LogcatSink.kt
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */


class LogcatSink: LogSink {
  override var logLevel: LogLevel = LogLevel.VERBOSE

  @SuppressLint("LogNotTimber")
  override fun println(level: LogLevel, tag: String, msg: String, throwable: Throwable?) {
    if (this.logLevel > level) return
    val fullTag = "[${Thread.currentThread().name}] $tag"
    when (level) {
      LogLevel.VERBOSE -> if (null == throwable) Log.v(fullTag, msg) else Log.v(fullTag, msg, throwable)
      LogLevel.DEBUG   -> if (null == throwable) Log.d(fullTag, msg) else Log.d(fullTag, msg, throwable)
      LogLevel.INFO    -> if (null == throwable) Log.i(fullTag, msg) else Log.i(fullTag, msg, throwable)
      LogLevel.WARN    -> if (null == throwable) Log.w(fullTag, msg) else Log.w(fullTag, msg, throwable)
      LogLevel.ERROR   -> if (null == throwable) Log.e(fullTag, msg) else Log.e(fullTag, msg, throwable)
    }
  }
}