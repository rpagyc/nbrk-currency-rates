package com.nbrk.rates.base

import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.nbrk.rates.BuildConfig

/**
* Created by Roman Shakirov on 14-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
class BaseApplication : MultiDexApplication() {

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)
    // Hack for screen share
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
  }
}