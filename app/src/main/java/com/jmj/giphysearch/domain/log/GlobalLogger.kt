package com.jmj.giphysearch.domain.log

import java.util.*

/**
 * Logger - A logging API that supports multiple outputs, verbosity filtering, and varargs string formatting.
 */
object GlobalLogger : Logger {

  private val sinks = mutableListOf<LogSink>()

  /**
   * Add a [LogSink].
   */
  fun addSink(sink: LogSink) {
    synchronized(sinks) {
      sinks.add(sink)
    }
  }

  /**
   * Clear all outputs.
   */
  fun clearSinks() {
    synchronized(sinks) {
      sinks.clear()
    }
  }

  /**
   * Set the global log level. This is applied to all current outputs.
   */
  fun setGlobalLevel(level: LogLevel) {
    synchronized(sinks) {
      for (output in sinks) {
        output.logLevel = level
      }
    }
  }

  /**
   * Print to the log using VERBOSE verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  override fun v(tag: String, msg: String) {
    outputsPrintln(LogLevel.VERBOSE, tag, msg)
  }

  /**
   * Print to the log using VERBOSE verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  override fun v(tag: String, fmt: String, vararg args: Any) {
    v(tag, String.format(fmt, *args))
  }

  /**
   * Print to the log using DEBUG verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  override fun d(tag: String, msg: String) {
    outputsPrintln(LogLevel.DEBUG, tag, msg)
  }

  /**
   * Print to the log using DEBUG verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  override fun d(tag: String, fmt: String, vararg args: Any) {
    d(tag, String.format(fmt, *args))
  }

  /**
   * Print to the log using INFO verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  override fun i(tag: String, msg: String) {
    outputsPrintln(LogLevel.INFO, tag, msg)
  }

  /**
   * Print to the log using INFO verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  override fun i(tag: String, fmt: String, vararg args: Any) {
    i(tag, String.format(fmt, *args))
  }

  /**
   * Print to the log using WARN verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  override fun w(tag: String, msg: String) {
    outputsPrintln(LogLevel.WARN, tag, msg)
  }

  /**
   * Same as [w], but also printing the stack trace of the provided Throwable.
   */
  override fun w(tag: String, throwable: Throwable, msg: String) {
    outputsPrintln(LogLevel.WARN, tag, msg, throwable)
  }

  /**
   * Print to the log using WARN verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  override fun w(tag: String, fmt: String, vararg args: Any) {
    w(tag, String.format(fmt, *args))
  }

  /**
   * Same as [w], but also printing the stack trace of the provided throwable.
   */
  override fun w(tag: String, throwable: Throwable, fmt: String, vararg args: Any) {
    w(tag, throwable, String.format(fmt, *args))
  }

  /**
   * Print to the log using ERROR verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  override fun e(tag: String, msg: String) {
    outputsPrintln(LogLevel.ERROR, tag, msg)
  }

  /**
   * Same as [e], but also printing the stack trace of the provided Throwable.
   */
  override fun e(tag: String, throwable: Throwable, msg: String) {
    outputsPrintln(LogLevel.ERROR, tag, msg, throwable)
  }

  /**
   * Print to the log using ERROR verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  override fun e(tag: String, fmt: String, vararg args: Any) {
    e(tag, String.format(fmt, *args))
  }

  /**
   * See [e]
   */
  override fun e(tag: String, throwable: Throwable, fmt: String, vararg args: Any) {
    e(tag, throwable, String.format(fmt, *args))
  }

  /**
   * Print to the log.
   *
   * @param level   The [LogLevel] to use.
   * @param tag     A log tag (usually class name).
   * @param message The message to log.
   */
  override fun println(level: LogLevel, tag: String, message: String) {
    outputsPrintln(level, tag, message)
  }

  private fun outputsPrintln(
    level: LogLevel,
    tag: String,
    msg: String,
    throwable: Throwable? = null
  ) =
    synchronized(sinks) { sinks.forEach { it.println(level, tag, msg, throwable) } }
}
