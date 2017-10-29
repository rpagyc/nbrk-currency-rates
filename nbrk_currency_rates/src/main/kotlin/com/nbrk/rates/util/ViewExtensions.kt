package com.nbrk.rates.util

import com.nbrk.rates.base.BaseApplication

fun String?.getDrawable(): Int {
  val ctx = BaseApplication.INSTANCE
  return ctx.resources.getIdentifier(this?.replace("TRY", "YTL")?.toLowerCase(),
    "drawable", ctx.packageName)
}

