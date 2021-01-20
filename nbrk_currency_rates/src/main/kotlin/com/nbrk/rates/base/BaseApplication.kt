package com.nbrk.rates.base

import android.app.Application
import android.os.StrictMode
import com.jakewharton.threetenabp.AndroidThreeTen

/**
* Created by Roman Shakirov on 14-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
class BaseApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    // Hack for screen share
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
  }
}