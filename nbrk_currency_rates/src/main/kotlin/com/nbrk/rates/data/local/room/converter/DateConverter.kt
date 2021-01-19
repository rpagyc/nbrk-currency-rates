package com.nbrk.rates.data.local.room.converter

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter


/**
 * Created by Roman Shakirov on 17-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class DateConverter {

  private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  @TypeConverter
  fun toLocalDate(value: String): LocalDate = formatter.parse(value, LocalDate::from)

  @TypeConverter
  fun fromLocalDate(date: LocalDate): String = date.format(formatter)
}