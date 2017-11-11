package com.nbrk.rates.base

/**
 * Created by Roman Shakirov on 02-Nov-17.
 * DigitTonic Studio
 * support@digittonic.com
 */

open class SingletonHolder<out T, in A>(private val creator: (A) -> T) {

  @Volatile private var instance: T? = null

  fun getInstance(arg: A): T {
    return instance ?: synchronized(this) {
      instance ?: creator(arg).also { instance = it }
    }
  }

}