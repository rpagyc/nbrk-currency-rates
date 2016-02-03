package com.nbrk.rates.data.db.resolvers

import com.nbrk.rates.data.db.tables.CurrencyRates
import com.nbrk.rates.home.model.entities.RatesItem
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery

/**
 * Created by rpagyc on 15-Jan-16.
 */
class RatesDeleteResolver : DefaultDeleteResolver<RatesItem>() {
  override fun mapToDeleteQuery(ratesItem: RatesItem): DeleteQuery {
    return DeleteQuery.builder()
      .table(CurrencyRates.TABLE_NAME)
      .where("${CurrencyRates.COL_CURRENCY_CODE} = ?" +
        " AND ${CurrencyRates.COL_DATE} = ?")
      .whereArgs(ratesItem.currencyCode, ratesItem.date)
      .build()
  }
}