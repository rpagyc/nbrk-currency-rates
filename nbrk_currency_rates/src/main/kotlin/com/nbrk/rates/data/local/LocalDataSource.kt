package com.nbrk.rates.data.local

import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.local.room.DatabaseCreator
import com.nbrk.rates.data.local.room.Mapper
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class LocalDataSource {

  private val mapper = Mapper()

  private val ratesDao = DatabaseCreator.database.ratesDao()

  fun getRates(date: Date): Flowable<List<RatesItem>> = ratesDao.getRates(date).map { it.map { mapper.transform(it) } }

  fun saveRates(rates: List<RatesItem>) = ratesDao.saveRates(rates.map { mapper.transform(it) })
}