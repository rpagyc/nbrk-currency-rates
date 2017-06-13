package com.nbrk.rates.home.model

import com.nbrk.rates.App
import com.nbrk.rates.data.rest.mappers.toDomain
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.entities.RatesItem
import io.reactivex.Single
import rx.Observable

/**
 * Created by rpagyc on 15-Jan-16.
 */
class RatesModel {

  companion object {
    val instance by lazy { RatesModel() }
  }

  fun getRates(date: String): Observable<Rates> {
    val dbRates = getDbRates(date)
    //val restRates = getRestRates(date)
    //return Observable.merge(dbRates, restRates).first()
    return dbRates
  }

  fun getDbRates(date: String): Observable<Rates> {
    return getRatesItems(date)
      .filter { it.isNotEmpty() }
      .map { ratesItems -> ratesItems.groupBy { it.date } }
      .map { groupedRates -> groupedRates.entries.map { Rates(it.key, it.value) } }
      .map { it[0] }
  }

  fun getRestRates(date: String): Single<Rates> {
    return App.restApi.getCurrencyRates(date)
      .map { it.toDomain() }
      //.doOnNext { putRates(it) }
  }

  fun getRatesItems(date: String): Observable<List<RatesItem>> {
    return Observable.just(emptyList<RatesItem>())
  }

    fun getRatesItems(date: String, range: Int, currency: String): Observable<List<RatesItem>> {
      return Observable.just(emptyList<RatesItem>())
    }

  fun putRates(rates: Rates): Unit {
  }

}