package com.nbrk.rates.base

import android.app.Application
import android.os.StrictMode
import com.facebook.stetho.Stetho
import com.nbrk.rates.BuildConfig
import com.nbrk.rates.data.local.room.DatabaseCreator
import com.nbrk.rates.util.DelegatesExt

/**
* Created by Roman Shakirov on 14-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
class BaseApplication : Application() {

  companion object {
    var INSTANCE: BaseApplication by DelegatesExt.notNullSingleValue()
  }

  override fun onCreate() {
    super.onCreate()
    INSTANCE = this
    DatabaseCreator.createDb(this)
    if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)
    // Hack for screen share
    val builder = StrictMode.VmPolicy.Builder()
    StrictMode.setVmPolicy(builder.build())
  }
}