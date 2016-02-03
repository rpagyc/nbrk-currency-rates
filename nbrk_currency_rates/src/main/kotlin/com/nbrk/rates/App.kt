package com.nbrk.rates

import android.app.Application
import com.facebook.stetho.Stetho
import com.nbrk.rates.data.db.DbOpenHelper
import com.nbrk.rates.data.db.resolvers.RatesDeleteResolver
import com.nbrk.rates.data.db.resolvers.RatesGetResolver
import com.nbrk.rates.data.db.resolvers.RatesPutResolver
import com.nbrk.rates.data.rest.RestApi
import com.nbrk.rates.extensions.DelegatesExt
import com.nbrk.rates.home.model.entities.RatesItem
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite

/**
 * Created by rpagyc on 14-Jan-16.
 */
class App : Application() {

  companion object {
    var instance: App by DelegatesExt.notNullSingleValue()
    val restApi by lazy { RestApi.create() }
    val storio by lazy {
      DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(DbOpenHelper.instance)
        .addTypeMapping(RatesItem::class.java, SQLiteTypeMapping.builder<RatesItem>()
          .putResolver(RatesPutResolver())
          .getResolver(RatesGetResolver())
          .deleteResolver(RatesDeleteResolver())
          .build())
        .build()
    }
  }

  override fun onCreate() {
    super.onCreate()
    instance = this
    if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this);
  }
}