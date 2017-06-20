package com.nbrk.rates.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by rpagyc on 15-Jan-16.
 */
@Entity(tableName = "dataSource")
data class RatesItem(
  @PrimaryKey(autoGenerate = true)
  var id: Long = 0,
  var date: Date = Calendar.getInstance().time,
  var currencyCode: String = "",
  var currencyName: String = "",
  var price: Double = .0,
  var quantity: Int = 0,
  var index: String = "",
  var change: Double = .0
)
