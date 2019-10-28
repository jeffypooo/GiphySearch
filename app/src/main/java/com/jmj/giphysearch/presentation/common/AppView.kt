package com.jmj.giphysearch.presentation.common

interface AppView {

  fun shortSnackbar(msg: String)

  fun longSnackbar(msg: String)

  fun dismiss()

}