package com.jmj.giphysearch.domain.log

/**
 *  ILogger.kt
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */


interface Logger {
  /**
   * Print to the log using VERBOSE verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  fun v(tag: String, msg: String)

  /**
   * Print to the log using VERBOSE verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  fun v(tag: String, fmt: String, vararg args: Any)

  /**
   * Print to the log using DEBUG verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  fun d(tag: String, msg: String)

  /**
   * Print to the log using DEBUG verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  fun d(tag: String, fmt: String, vararg args: Any)

  /**
   * Print to the log using INFO verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  fun i(tag: String, msg: String)

  /**
   * Print to the log using INFO verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  fun i(tag: String, fmt: String, vararg args: Any)

  /**
   * Print to the log using WARN verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  fun w(tag: String, msg: String)

  /**
   * Same as [w], but also printing the stack trace of the provided Throwable.
   */
  fun w(tag: String, throwable: Throwable, msg: String)

  /**
   * Print to the log using WARN verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  fun w(tag: String, fmt: String, vararg args: Any)

  /**
   * Same as [w], but also printing the stack trace of the provided throwable.
   */
  fun w(tag: String, throwable: Throwable, fmt: String, vararg args: Any)

  /**
   * Print to the log using ERROR verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param msg The message to log.
   */
  fun e(tag: String, msg: String)

  /**
   * Same as [e], but also printing the stack trace of the provided Throwable.
   */
  fun e(tag: String, throwable: Throwable, msg: String)

  /**
   * Print to the log using ERROR verbosity level.
   *
   * @param tag A log tag (usually class name).
   * @param fmt A format string.
   * @param args Arguments for specifiers in the format string.
   */
  fun e(tag: String, fmt: String, vararg args: Any)

  /**
   * See [e]
   */
  fun e(tag: String, throwable: Throwable, fmt: String, vararg args: Any)

  /**
   * Print to the log.
   *
   * @param level   The [LogLevel] to use.
   * @param tag     A log tag (usually class name).
   * @param message The message to log.
   */
  fun println(level: LogLevel, tag: String, message: String)
}