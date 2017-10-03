package com.nbrk.rates.data

import com.nbrk.rates.data.db.DatabaseCreator
import com.nbrk.rates.entities.RatesItem
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class LocalDataSource {

  private val ratesDao = DatabaseCreator.database.ratesDao()

  fun getRates(date: Date): Flowable<List<RatesItem>> = ratesDao.getRates(date)

  fun saveRates(rates: List<RatesItem>) = ratesDao.saveRates(rates)
}