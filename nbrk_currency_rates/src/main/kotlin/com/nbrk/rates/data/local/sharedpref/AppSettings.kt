package com.nbrk.rates.data.local.sharedpref

import android.content.Context
import com.nbrk.rates.base.BaseApplication
import org.jetbrains.anko.defaultSharedPreferences

/**
 * Created by Roman Shakirov on 29-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
class AppSettings(private val context: Context = BaseApplication.INSTANCE) {

  fun isCurrencyVisibleInApp(currencyCode: String): Boolean {
    return context.defaultSharedPreferences.getBoolean("pref_key_show_$currencyCode", true)
  }

  fun isCurrencyVisibleInWidget(currencyCode: String): Boolean {
    return context.defaultSharedPreferences.getBoolean("widget_show_$currencyCode", true)
  }

}