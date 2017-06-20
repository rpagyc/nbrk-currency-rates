package com.nbrk.rates.data

import android.annotation.SuppressLint
import com.nbrk.rates.entities.Rates
import io.reactivex.Single
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
interface RatesDataSource {

  fun getRates(date: Date): Single<Rates>

  @SuppressLint("NewApi")
  fun saveRates(rates: Rates): Unit = Unit
}