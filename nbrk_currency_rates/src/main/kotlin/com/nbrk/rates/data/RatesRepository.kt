package com.nbrk.rates.data

import com.nbrk.rates.data.local.LocalDataSource
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.local.sharedpref.AppSettings
import com.nbrk.rates.data.remote.RemoteDataSource
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRepository(
  private val localDataSource: LocalDataSource = LocalDataSource(),
  private val remoteDataSource: RemoteDataSource = RemoteDataSource(),
  private val appSettings: AppSettings = AppSettings()
) {

  fun getRates(date: Date): Flowable<List<RatesItem>> {
    return Flowable.concat(
      localDataSource.getRates(date).takeWhile { it.isNotEmpty() },
      remoteDataSource.getRates(date).doOnNext { localDataSource.saveRates(it) })
      .distinctUntilChanged()
      .map { it.filter { appSettings.isCurrencyVisibleInApp(it.currencyCode) } }
  }

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