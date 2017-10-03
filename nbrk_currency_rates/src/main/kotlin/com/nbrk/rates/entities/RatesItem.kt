package com.nbrk.rates.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
* Created by Roman Shakirov on 15-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
@Entity(tableName = "rates")
data class RatesItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long,
  val date: Date,
  val currencyCode: String,
  val currencyName: String,
  val price: Double,
  val quantity: Int,
  val index: String,
  val change: Double
)
