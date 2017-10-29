package com.nbrk.rates.data.local.domain.model

import java.util.*

/**
* Created by Roman Shakirov on 15-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
data class RatesItem(
  val date: Date,
  val currencyCode: String,
  val currencyName: String,
  val price: Double,
  val quantity: Int,
  val index: String,
  val change: Double
)
