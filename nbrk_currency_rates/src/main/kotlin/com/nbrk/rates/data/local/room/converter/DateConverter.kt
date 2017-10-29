package com.nbrk.rates.data.local.room.converter

import android.arch.persistence.room.TypeConverter
import java.util.*


/**
 * Created by Roman Shakirov on 17-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class DateConverter {
  @TypeConverter
  fun fromTimestamp(value: Long): Date = Date(value)

  @TypeConverter
  fun dateToTimestamp(date: Date): Long {
    val cal = Calendar.getInstance().apply {
      time = date
      set(Calendar.HOUR_OF_DAY, 0)
      set(Calendar.MINUTE, 0)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
    }
    return cal.time.time
  }
}