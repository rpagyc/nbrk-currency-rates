package com.nbrk.rates.data.local.domain.model

import org.threeten.bp.LocalDate

/**
* Created by Roman Shakirov on 15-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
data class RatesItem(
  val date: LocalDate,
  val currencyCode: String,
  val currencyName: String,
  val price: Double,
  val quantity: Int,
  val index: String,
  val change: Double
)
