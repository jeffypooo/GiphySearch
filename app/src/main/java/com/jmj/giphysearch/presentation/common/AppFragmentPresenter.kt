package com.jmj.giphysearch.presentation.common


abstract class AppFragmentPresenter<V: AppView>() {

  protected var view: V? = null

  open fun onViewCreated(view: V) {
    this.view = view
  }

  open fun onDestroyView() {
    this.view = null
  }

  open fun onStart() = Unit

  open fun onStop() = Unit

  open fun onResume() = Unit

  open fun onPause() = Unit

}