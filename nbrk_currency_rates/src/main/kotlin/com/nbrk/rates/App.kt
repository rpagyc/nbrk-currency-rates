package com.nbrk.rates

import android.app.Application
import com.facebook.stetho.Stetho
import com.nbrk.rates.data.db.DatabaseCreator
import com.nbrk.rates.data.rest.RestApi
import com.nbrk.rates.extensions.DelegatesExt

/**
 * Created by rpagyc on 14-Jan-16.
 */
class App : Application() {

  companion object {
    var instance: App by DelegatesExt.notNullSingleValue()
    val restApi by lazy { RestApi.create() }
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
    DatabaseCreator.createDb(this)
    if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this)
  }
}