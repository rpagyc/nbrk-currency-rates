package com.nbrk.rates.data

import com.nbrk.rates.data.rest.RestApi
import com.nbrk.rates.data.rest.mappers.toEntity
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.extensions.toDateString
import io.reactivex.Single
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRemoteDataSource : RatesDataSource {

  override fun getRates(date: Date): Single<Rates> = RestApi
    .create()
    .getCurrencyRates(date.toDateString())
    .map { it.toEntity() }

}