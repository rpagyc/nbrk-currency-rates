package com.nbrk.rates.data.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.nbrk.rates.base.SingletonHolder
import com.nbrk.rates.data.local.room.converter.DateConverter
import com.nbrk.rates.data.local.room.dao.RatesDao
import com.nbrk.rates.data.local.room.model.RoomRatesItem

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
@Database(entities = [RoomRatesItem::class], version = 3, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun ratesDao(): RatesDao

  companion object : SingletonHolder<AppDatabase, Context>({
    Room.databaseBuilder(it.applicationContext, AppDatabase::class.java, "currency_rates")
      .fallbackToDestructiveMigration()
      .build()
  })
}