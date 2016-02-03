package com.nbrk.rates.extensions

import android.content.Context
import android.view.View
import com.nbrk.rates.App

val View.ctx: Context
  get() = context

fun String.getDrawable(): Int {
  val ctx = App.instance
  return ctx.resources.getIdentifier(this.replace("TRY", "YTL").toLowerCase(), "drawable", ctx.packageName)
}

