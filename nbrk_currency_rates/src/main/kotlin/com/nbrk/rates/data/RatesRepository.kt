package com.nbrk.rates.data

import com.nbrk.rates.entities.RatesItem
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRepository {

  private val localDataSource = LocalDataSource()
  private val remoteDataSource = RemoteDataSource()

  fun getRates(date: Date): Flowable<List<RatesItem>> = Flowable.concat(
    localDataSource.getRates(date).takeWhile { it.isNotEmpty() },
    remoteDataSource.getRates(date).doOnNext { localDataSource.saveRates(it) })
    .distinctUntilChanged()

  fun getChartRates(currency: String, period: Int): Flowable<List<RatesItem>> {

    val startDate = Calendar.getInstance()
    startDate.add(Calendar.DATE, -period)

    return Flowable
      .fromIterable(1..period)
      .map {
        startDate.add(Calendar.DATE, 1)
        startDate.apply {
          set(Calendar.HOUR_OF_DAY, 0)
          set(Calendar.MINUTE, 0)
          set(Calendar.SECOND, 0)
          set(Calendar.MILLISECOND, 0)
        }
        startDate.time
      }
      .flatMap { getRates(it).take(1) }
      .map { it.first { it.currencyCode == currency } }
      .toList()
      .toFlowable()
  }
}