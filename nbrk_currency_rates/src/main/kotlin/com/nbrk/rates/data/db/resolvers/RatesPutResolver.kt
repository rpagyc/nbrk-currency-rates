package com.nbrk.rates.data.db.resolvers

import android.content.ContentValues
import com.nbrk.rates.data.db.tables.CurrencyRates
import com.nbrk.rates.home.model.entities.RatesItem
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver
import com.pushtorefresh.storio.sqlite.queries.InsertQuery
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery

/**
 * Created by rpagyc on 15-Jan-16.
 */
class RatesPutResolver : DefaultPutResolver<RatesItem>() {

  override fun mapToInsertQuery(ratesItem: RatesItem): InsertQuery {
    return InsertQuery.builder()
      .table(CurrencyRates.TABLE_NAME)
      .build()
  }

  override fun mapToUpdateQuery(ratesItem: RatesItem): UpdateQuery {
    return UpdateQuery.builder()
      .table(CurrencyRates.TABLE_NAME)
      .where("${CurrencyRates.COL_CURRENCY_CODE} = ?" +
        " AND ${CurrencyRates.COL_DATE} = ?")
      .whereArgs(ratesItem.currencyCode,
        ratesItem.date)
      .build()
  }

  override fun mapToContentValues(ratesItem: RatesItem): ContentValues {
    val cv = ContentValues()
    cv.put(CurrencyRates.COL_DATE, ratesItem.date)
    cv.put(CurrencyRates.COL_CURRENCY_CODE, ratesItem.currencyCode)
    cv.put(CurrencyRates.COL_CURRENCY_NAME, ratesItem.currencyName)
    cv.put(CurrencyRates.COL_QUANTITY, ratesItem.quantity)
    cv.put(CurrencyRates.COL_PRICE, ratesItem.price)
    cv.put(CurrencyRates.COL_INDEX, ratesItem.index)
    cv.put(CurrencyRates.COL_CHANGE, ratesItem.change)
    return cv
  }

}