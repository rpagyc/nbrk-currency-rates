package com.nbrk.rates

import android.content.Context
import com.nbrk.rates.data.RatesRepository

/**
 * Created by Roman Shakirov on 02-Nov-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
object Injection {

  fun provideRatesRepository(context: Context): RatesRepository = RatesRepository(context)

  fun provideViewModelFactory(context: Context): ViewModelFactory {
    val repository = provideRatesRepository(context)
    return ViewModelFactory(repository)
  }
}