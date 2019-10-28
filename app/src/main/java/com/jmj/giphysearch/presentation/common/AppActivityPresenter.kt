package com.jmj.giphysearch.presentation.common


/**
 * Base class for activity presenters.
 * @param V View type.
 */
abstract class AppActivityPresenter<V : AppView> {

  protected var view: V? = null

  open fun onCreate(view: V) {
    this.view = view
  }

  open fun onStart() = Unit

  open fun onResume() = Unit

  open fun onPause() = Unit

  open fun onStop() = Unit

  open fun onDestroy() {
    view = null
  }

  open fun onAlertDismissed(id: Int) = Unit
}