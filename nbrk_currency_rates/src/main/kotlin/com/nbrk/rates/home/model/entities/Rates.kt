package com.nbrk.rates.home.model.entities

/**
 * Created by rpagyc on 15-Jan-16.
 */
data class Rates(
  val date: Long,
  val rates: List<RatesItem>
)