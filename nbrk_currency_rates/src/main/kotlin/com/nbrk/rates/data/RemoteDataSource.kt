package com.nbrk.rates.data

import com.nbrk.rates.data.rest.RestApi
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.toDate
import com.nbrk.rates.extensions.toDateString
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RemoteDataSource {
  fun getRates(date: Date): Flowable<List<RatesItem>> = RestApi
    .create()
    .getCurrencyRates(date.toDateString())
    .map { rates ->
      rates
        .currencyRatesItemList
        .map { item ->
          RatesItem(
            id = 0,
            change = item.change.toDouble(),
            index = item.index,
            date = rates.date.toDate(),
            quantity = item.quant.toInt(),
            price = item.description.toDouble(),
            currencyCode = item.title,
            currencyName = item.fullname
          )
        }
    }
    .onErrorReturn { emptyList() }
}