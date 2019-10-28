package com.jmj.giphysearch.presentation.common

import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class AppBottomSheetDialogFragment<P : AppFragmentPresenter<*>> : BottomSheetDialogFragment() {

  abstract val presenter: P

  override fun onDestroyView() {
    super.onDestroyView()
    presenter.onDestroyView()
  }

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


}