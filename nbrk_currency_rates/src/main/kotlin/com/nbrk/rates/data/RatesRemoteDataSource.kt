package com.nbrk.rates.data

import com.nbrk.rates.data.rest.RestApi
import com.nbrk.rates.data.rest.mappers.toDomain
import com.nbrk.rates.entities.Rates
import io.reactivex.Single

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRemoteDataSource : RatesDataSource {
  
  override fun getRates(date: String): Single<Rates> = RestApi.create().getCurrencyRates(date).map { it.toDomain() }

}