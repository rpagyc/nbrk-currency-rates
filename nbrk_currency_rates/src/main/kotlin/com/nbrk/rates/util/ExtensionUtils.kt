package com.nbrk.rates.util

import android.util.Log
import com.nbrk.rates.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

/**
* Created by Roman Shakirov on 16-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
fun String.toDate(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy",
  Locale.getDefault())): Date {
  return sdf.parse(this)
}

fun Date.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy",
  Locale.getDefault())): String {
  this.toString()
  return sdf.format(this)
}

fun Any.TAG(): String = this.javaClass.simpleName

fun <T> Any.debug(item: T) {
  if (BuildConfig.DEBUG)
    Log.d(this.TAG(), item.toString())
}

fun <T> Any.error(item: T) {
  if (BuildConfig.DEBUG)
    Log.e(this.TAG(), item.toString())
}