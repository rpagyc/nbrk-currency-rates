package com.nbrk.rates.extensions

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import com.nbrk.rates.BuildConfig
import com.nbrk.rates.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rpagyc on 16-Jan-16.
 */
fun String.toDateLong(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())): Long {
  val date = sdf.parse(this)
  return date.time
}

fun String.toDate(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())): Date {
  return sdf.parse(this)
}

fun Date.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())): String {
  this.toString()
  return sdf.format(this)
}

fun Long.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("d MMM yyyy", Locale.getDefault())): String {
  return sdf.format(this)
}

fun Calendar.toDateString(sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())): String {
  return sdf.format(this.time)
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