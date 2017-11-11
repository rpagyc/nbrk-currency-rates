package com.nbrk.rates.data.local.room.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.threeten.bp.LocalDate

/**
 * Created by Roman Shakirov on 27-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */

@Entity(tableName = "rates")
data class RoomRatesItem(
  @PrimaryKey(autoGenerate = true)
  val id: Long,
  val date: LocalDate,
  val currencyCode: String,
  val currencyName: String,
  val price: Double,
  val quantity: Int,
  val index: String,
  val change: Double
)
