package com.nbrk.rates.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
* Created by Roman Shakirov on 14-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
object DelegatesExt {

  fun <T> notNullSingleValue(): ReadWriteProperty<Any?, T> = NotNullSingleValue()

  fun preference(context: Context, name: String, default: Any)
    = Preference(context, name, default)

}

private class NotNullSingleValue<T> : ReadWriteProperty<Any?, T> {

  private var value: T? = null

  override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return value ?: throw IllegalStateException("${property.name} " +
      "not initialized")
  }

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    this.value = if (this.value == null) value
    else throw IllegalStateException("${property.name} already initialized")
  }
}

class Preference<T>(val context: Context, val name: String, val default: T)
  : ReadWriteProperty<Any?, T> {

  private val prefs: SharedPreferences by lazy {
    context.getSharedPreferences("default", Context.MODE_PRIVATE)
  }

  override fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return findPreference(name, default)
  }

  override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    putPreference(name, value)
  }

  private fun <T> findPreference(name: String, default: T): T = with(prefs) {
    val res: Any = when (default) {
      is Long -> getLong(name, default)
      is String -> getString(name, default)
      is Int -> getInt(name, default)
      is Boolean -> getBoolean(name, default)
      is Float -> getFloat(name, default)
      else -> throw IllegalArgumentException("This type can be saved into Preferences")
    }!!
    @Suppress("UNCHECKED_CAST")
    res as T
  }

  private fun <U> putPreference(name: String, value: U) = with(prefs.edit()) {
    when (value) {
      is Long -> putLong(name, value)
      is String -> putString(name, value)
      is Int -> putInt(name, value)
      is Boolean -> putBoolean(name, value)
      is Float -> putFloat(name, value)
      else -> throw IllegalArgumentException("This type can be saved into Preferences")
    }.apply()
  }
}