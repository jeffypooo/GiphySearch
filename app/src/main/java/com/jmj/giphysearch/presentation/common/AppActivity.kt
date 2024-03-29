package com.jmj.giphysearch.presentation.common

import androidx.appcompat.app.AppCompatActivity

abstract class AppActivity<P : AppActivityPresenter<*>> : AppCompatActivity() {
  abstract val presenter: P

  override fun onStart() {
    super.onStart()
    presenter.onStart()
  }

  override fun onStop() {
    super.onStop()
    presenter.onStop()
  }

  override fun onResume() {
    super.onResume()
    presenter.onResume()
  }

  override fun onPause() {
    super.onPause()
    presenter.onPause()
  }

  override fun onDestroy() {
    super.onDestroy()
    presenter.onDestroy()
  }
}