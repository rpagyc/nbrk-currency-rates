package com.nbrk.rates.data

import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.local.room.model.RoomRatesItem
import com.nbrk.rates.data.remote.rest.model.RestRates
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Roman Shakirov on 11-Nov-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
class Mapper {

  fun restRatesToRoom(restRates: RestRates): List<RoomRatesItem> {

    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    return restRates.restRatesItemList
      .map { item ->
        RoomRatesItem(
          id = 0,
          change = item.change.toDouble(),
          index = item.index,
          date = dateTimeFormatter.parse(restRates.date, LocalDate::from),
          quantity = item.quant.toInt(),
          price = item.description.toDouble(),
          currencyCode = item.title,
          currencyName = item.fullname
        )
      }
  }

  fun roomRatesToDomain(roomRates: List<RoomRatesItem>): List<RatesItem> {
    return roomRates.map { roomRatesItem ->
      RatesItem(
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
}