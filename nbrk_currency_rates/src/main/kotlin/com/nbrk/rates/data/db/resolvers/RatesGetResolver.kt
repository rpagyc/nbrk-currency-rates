package com.nbrk.rates.data.db.resolvers

import android.database.Cursor
import com.nbrk.rates.data.db.tables.CurrencyRates
import com.nbrk.rates.home.model.entities.RatesItem
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver

/**
 * Created by rpagyc on 15-Jan-16.
 */
class RatesGetResolver : DefaultGetResolver<RatesItem>() {

  override fun mapFromCursor(cursor: Cursor): RatesItem {
    val activity = RatesItem(
      date = cursor.getLong(cursor.getColumnIndex(CurrencyRates.COL_DATE)),
      currencyCode = cursor.getString(cursor.getColumnIndex(CurrencyRates.COL_CURRENCY_CODE)),
      currencyName = cursor.getString(cursor.getColumnIndex(CurrencyRates.COL_CURRENCY_NAME)),
      price = cursor.getDouble(cursor.getColumnIndex(CurrencyRates.COL_PRICE)),
      quantity = cursor.getInt(cursor.getColumnIndex(CurrencyRates.COL_QUANTITY)),
      index = cursor.getString(cursor.getColumnIndex(CurrencyRates.COL_INDEX)),
      change = cursor.getDouble(cursor.getColumnIndex(CurrencyRates.COL_CHANGE))
    )
    return activity;
  }

}