package com.nbrk.rates.data.local.room

import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.local.room.model.RoomRatesItem

/**
 * Created by Roman Shakirov on 27-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class Mapper {

  fun transform(ratesItem: RatesItem): RoomRatesItem {
    return RoomRatesItem(
      0,
      date = ratesItem.date,
      currencyCode = ratesItem.currencyCode,
      currencyName = ratesItem.currencyName,
      price = ratesItem.price,
      quantity = ratesItem.quantity,
      index = ratesItem.index,
      change = ratesItem.change
    )
  }

  fun transform(roomRatesItem: RoomRatesItem): RatesItem {
    return RatesItem(
      date = roomRatesItem.date,
      currencyCode = roomRatesItem.currencyCode,
      currencyName = roomRatesItem.currencyName,
      price = roomRatesItem.price,
      quantity = roomRatesItem.quantity,
      index = roomRatesItem.index,
      change = roomRatesItem.change
    )
  }

}