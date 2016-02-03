package com.nbrk.rates.extensions

import android.util.Log
import com.nbrk.rates.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rpagyc on 16-Jan-16.
 */
fun String.toDateLong(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")): Long {
  val date = sdf.parse(this)
  return date.time
}

fun Long.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("d MMM yyyy")): String {
  return sdf.format(this)
}

fun Calendar.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy")): String {
  return sdf.format(this.time)
}

val Any.TAG: String
  get() = this.javaClass.simpleName

fun <T> Any.debug(item: T) {
  if (BuildConfig.DEBUG)
    Log.d(TAG, item.toString())
}

fun <T> Any.error(item: T) {
  if (BuildConfig.DEBUG)
    Log.e(TAG, item.toString())
}