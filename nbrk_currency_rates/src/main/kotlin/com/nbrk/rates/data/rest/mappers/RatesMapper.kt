package com.nbrk.rates.data.rest.mappers

import com.nbrk.rates.data.rest.entities.CurrencyRates
import com.nbrk.rates.extensions.toDateLong
import com.nbrk.rates.home.model.entities.Rates
import com.nbrk.rates.home.model.entities.RatesItem

/**
 * Created by rpagyc on 15-Jan-16.
 */
fun CurrencyRates.toDomain(): Rates = Rates (
  date = date.toDateLong(),
  rates = currencyRatesItemList.map {
    RatesItem(
      date = date.toDateLong(),
      currencyCode = it.title,
      currencyName = it.fullname,
      price = it.description.toDouble(),
      quantity = it.quant.toInt(),
      index = it.ind,
      change = it.change.toDouble()
    )
  }
)