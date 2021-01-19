package com.nbrk.rates.base

import android.os.StrictMode
import androidx.multidex.MultiDexApplication
import com.jakewharton.threetenabp.AndroidThreeTen

/**
* Created by Roman Shakirov on 14-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
class BaseApplication : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    // Hack for screen share
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
  }
}