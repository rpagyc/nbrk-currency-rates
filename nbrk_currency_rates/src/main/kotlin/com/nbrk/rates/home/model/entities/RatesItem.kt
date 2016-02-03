package com.nbrk.rates.home.model.entities

/**
 * Created by rpagyc on 15-Jan-16.
 */
data class RatesItem(
  val date: Long,
  val currencyCode: String,
  val currencyName: String,
  val price: Double,
  val quantity: Int,
  val index: String,
  val change: Double
)
