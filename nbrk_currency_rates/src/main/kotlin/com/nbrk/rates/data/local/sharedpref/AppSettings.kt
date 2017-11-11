package com.nbrk.rates.data.local.sharedpref

import android.content.Context
import org.jetbrains.anko.defaultSharedPreferences

/**
 * Created by Roman Shakirov on 29-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
class AppSettings(context: Context) {

  private val sharedPref = context.defaultSharedPreferences

  fun isVisibleInApp(currencyCode: String): Boolean =
    sharedPref.getBoolean("pref_key_show_$currencyCode", true)

  fun isVisibleInWidget(currencyCode: String): Boolean =
    sharedPref.getBoolean("widget_show_$currencyCode", true)

}