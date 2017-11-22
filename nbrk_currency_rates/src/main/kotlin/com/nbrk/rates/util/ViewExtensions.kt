package com.nbrk.rates.util

import android.content.Context

fun Context.getImageId(fileName: String): Int {
  return this.resources.getIdentifier(fileName.replace("TRY", "YTL").toLowerCase(),
    "drawable", this.packageName)
}

