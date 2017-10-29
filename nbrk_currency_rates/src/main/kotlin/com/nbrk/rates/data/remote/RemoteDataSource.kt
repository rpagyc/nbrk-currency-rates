package com.nbrk.rates.data.remote

import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.remote.rest.Mapper
import com.nbrk.rates.data.remote.rest.RestApi
import com.nbrk.rates.util.toDateString
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RemoteDataSource {

  private val mapper = Mapper()

  fun getRates(date: Date): Flowable<List<RatesItem>> = RestApi
    .create()
    .getCurrencyRates(date.toDateString())
    .map { mapper.transform(it) }
    .onErrorReturn { emptyList() }
}