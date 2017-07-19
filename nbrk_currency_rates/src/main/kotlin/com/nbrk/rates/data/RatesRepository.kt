package com.nbrk.rates.data

import android.preference.PreferenceManager
import com.nbrk.rates.App
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.entities.RatesItem
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRepository : RatesDataSource {

  val remoteDataSource = RatesRemoteDataSource()
  val localDataSource = RatesLocalDataSource()
  val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(App.instance)

  override fun getRates(date: Date): Single<Rates> =
    localDataSource
      .getRates(date)
      .onErrorResumeNext {
        remoteDataSource.getRates(date)
          .doOnSuccess { localDataSource.saveRates(it) }
      }
      .map {
        Rates(it.date, it.rates.filter { sharedPrefs.getBoolean("pref_key_show_${it.currencyCode}", true) })
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

  fun getRates(currency: String, period: Int): Single<List<RatesItem>> {

    val startDate = Calendar.getInstance()
    startDate.add(Calendar.DATE, -period)

    val dates = Flowable
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

    val localRates = localDataSource
      .getRates(currency, period)
      .take(1)
      .flatMapIterable { it }

    val rates = dates
      .flatMap { date ->
        localRates
          .any { it.date == date }
          .toFlowable()
          .flatMap { exist ->
            if (exist) {
              localRates
                .filter { it.date == date }
            } else {
              remoteDataSource
                .getRates(date)
                .doOnSuccess { localDataSource.saveRates(it) }
                .map { it.rates.single { it.currencyCode == currency } }
                .toFlowable()
            }
          }
      }
      .toList()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())

    return rates
  }

}
