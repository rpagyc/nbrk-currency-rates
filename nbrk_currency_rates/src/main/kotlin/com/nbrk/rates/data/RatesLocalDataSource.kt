package com.nbrk.rates.data

import com.nbrk.rates.data.db.DatabaseCreator
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.extensions.toDateLong
import io.reactivex.Single

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesLocalDataSource : RatesDataSource {

  val ratesDao = DatabaseCreator.database.ratesDao()

  override fun getRates(date: String): Single<Rates> =
    ratesDao
      .getRates(date.toDateLong())
      .firstOrError()
      .doOnSuccess { if (it.isEmpty()) throw Exception() }
      .map { Rates(date.toDateLong(), it) }

  override fun saveRates(rates: Rates) = ratesDao.saveRates(rates.rates)
}