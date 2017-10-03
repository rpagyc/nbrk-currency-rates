package com.nbrk.rates.extensions

import com.nbrk.rates.App

fun String?.getDrawable(): Int {
  val ctx = App.instance
  return ctx.resources.getIdentifier(this?.replace("TRY", "YTL")?.toLowerCase(), "drawable", ctx.packageName)
}

