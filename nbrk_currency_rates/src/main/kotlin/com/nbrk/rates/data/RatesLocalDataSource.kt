package com.nbrk.rates.data

import com.nbrk.rates.data.db.DatabaseCreator
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.entities.RatesItem
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesLocalDataSource : RatesDataSource {

  private val ratesDao = DatabaseCreator.database.ratesDao()

  override fun getRates(date: Date): Single<Rates> =
    ratesDao
      .getRates(date)
      .firstOrError()
      .doOnSuccess { if (it.isEmpty()) throw Exception() }
      .map { Rates(date, it) }

  fun getRates(currency: String, period: Int): Flowable<List<RatesItem>> {
    val startDate = Calendar.getInstance()
    startDate.add(Calendar.DATE, -period)
    val endDate = Calendar.getInstance()

    return ratesDao
      .getRates(startDate.time, endDate.time, currency)
  }

  override fun saveRates(rates: Rates) = ratesDao.saveRates(rates.rates)
}