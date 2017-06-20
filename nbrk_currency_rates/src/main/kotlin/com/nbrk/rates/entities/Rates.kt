package com.nbrk.rates.entities

import java.util.*

/**
 * Created by rpagyc on 15-Jan-16.
 */
data class Rates(
  val date: Date,
  val rates: List<RatesItem>
)