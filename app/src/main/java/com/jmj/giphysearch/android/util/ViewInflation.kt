package com.jmj.giphysearch.android.util

import android.view.LayoutInflater
import android.view.View

fun <T : View> layoutInflater(view: T) = LayoutInflater.from(view.context)!!