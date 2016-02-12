package com.nbrk.rates.home.model

import com.nbrk.rates.App
import com.nbrk.rates.data.db.tables.CurrencyRates
import com.nbrk.rates.data.rest.mappers.toDomain
import com.nbrk.rates.extensions.toDateLong
import com.nbrk.rates.home.model.entities.Rates
import com.nbrk.rates.home.model.entities.RatesItem
import com.pushtorefresh.storio.sqlite.operations.put.PutResults
import com.pushtorefresh.storio.sqlite.queries.Query
import rx.Observable
import java.util.*

/**
 * Created by rpagyc on 15-Jan-16.
 */
class RatesModel {

  companion object {
    val instance by lazy { RatesModel() }
  }

  fun getRates(date: String): Observable<Rates> {
    val dbRates = getDbRates(date)
    val restRates = getRestRates(date)
    return Observable.merge(dbRates, restRates).first()
  }

  fun getDbRates(date: String): Observable<Rates> {
    return getRatesItems(date)
      .filter { it.isNotEmpty() }
      .map { ratesItems -> ratesItems.groupBy { it.date } }
      .map { groupedRates -> groupedRates.entries.map { Rates(it.key, it.value) } }
      .map { it[0] }
  }

  fun getRestRates(date: String): Observable<Rates> {
    return App.restApi.getCurrencyRates(date)
      .filter { it.isSuccess }
      .map { it.body().toDomain() }
      .doOnNext { putRates(it) }
  }

  fun getRatesItems(date: String): Observable<List<RatesItem>> {
    return App.storio
      .get()
      .listOfObjects(RatesItem::class.java)
      .withQuery(Query.builder()
        .table(CurrencyRates.TABLE_NAME)
        .where("${CurrencyRates.COL_DATE} = ?")
        .whereArgs(date.toDateLong())
        .build())
      .prepare()
      .createObservable()
  }

    fun getRatesItems(date: String, range: Int, currency: String): Observable<List<RatesItem>> {
      val startDate = Calendar.getInstance()
      startDate.add(Calendar.DATE, -range)
      return App.storio
        .get()
        .listOfObjects(RatesItem::class.java)
        .withQuery(Query.builder()
          .table(CurrencyRates.TABLE_NAME)
          .where("${CurrencyRates.COL_DATE} BETWEEN ? AND ? AND ${CurrencyRates.COL_CURRENCY_CODE} = ?")
          .whereArgs(startDate.timeInMillis, date.toDateLong(), currency)
          .build())
        .prepare()
        .createObservable()
    }

  fun putRates(rates: Rates): PutResults<RatesItem> {
    return App.storio
      .put()
      .objects(rates.rates)
      .prepare()
      .executeAsBlocking()
  }

}