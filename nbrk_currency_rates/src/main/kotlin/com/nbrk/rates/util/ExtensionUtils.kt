package com.nbrk.rates.util

import android.util.Log
import com.nbrk.rates.BuildConfig

/**
* Created by Roman Shakirov on 16-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/

fun Any.TAG(): String = this.javaClass.simpleName

fun <T> Any.debug(item: T) {
  if (BuildConfig.DEBUG)
    Log.d(this.TAG(), item.toString())
}

fun <T> Any.error(item: T) {
  if (BuildConfig.DEBUG)
    Log.e(this.TAG(), item.toString())
}