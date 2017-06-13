package com.nbrk.rates.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.nbrk.rates.data.db.dao.RatesDao
import com.nbrk.rates.entities.RatesItem

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
@Database(entities = arrayOf(RatesItem::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

  abstract fun ratesDao() : RatesDao

  companion object {
    const val DATABASE_NAME = "currency_rates"
  }
}