package com.nbrk.rates.data

import com.nbrk.rates.entities.Rates
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRepository : RatesDataSource {

  private val remoteDataSource = RatesRemoteDataSource()
  private val localDataSource = RatesLocalDataSource()

  override fun getRates(date: String): Single<Rates> =
    localDataSource
      .getRates(date)
      .onErrorResumeNext {
        remoteDataSource.getRates(date)
          .doOnSuccess { localDataSource.saveRates(it) }
      }
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
}