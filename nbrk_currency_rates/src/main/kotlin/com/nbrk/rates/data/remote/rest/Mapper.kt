package com.nbrk.rates.data.remote.rest

import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.remote.rest.model.RestRates
import com.nbrk.rates.util.toDate

/**
 * Created by Roman Shakirov on 23-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class Mapper {

  fun transform(restRates: RestRates): List<RatesItem> {
    return restRates.restRatesItemList
      .map { item ->
        RatesItem(
          change = item.change.toDouble(),
          index = item.index,
          date = restRates.date.toDate(),
          quantity = item.quant.toInt(),
          price = item.description.toDouble(),
          currencyCode = item.title,
          currencyName = item.fullname
        )
      }
  }
}