package com.jmj.giphysearch.presentation.common

import androidx.appcompat.app.AppCompatActivity

/**
 *  AppActivity
 *  author:  jefferson jones
 *  org:     Beartooth, Inc
 *  github:  github.com/masterjefferson
 *  email:   jeff@beartooth.com
 */
abstract class AppActivity<P: AppActivityPresenter<*>>: AppCompatActivity() {
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