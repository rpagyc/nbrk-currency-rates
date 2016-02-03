package com.nbrk.rates.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.nbrk.rates.App
import com.nbrk.rates.data.db.tables.CurrencyRates
import org.jetbrains.anko.db.*

class DbOpenHelper(context: Context = App.instance) :
  ManagedSQLiteOpenHelper(context, DbOpenHelper.DATABASE_NAME, null, DbOpenHelper.DATABASE_VERSION) {

  companion object {
    private val DATABASE_NAME = "currency_rates.db"
    private val DATABASE_VERSION = 5
    val instance: DbOpenHelper by lazy { DbOpenHelper() }
  }

  override fun onCreate(db: SQLiteDatabase) {
    db.createTable(CurrencyRates.TABLE_NAME, true,
      CurrencyRates.COL_DATE to INTEGER,
      CurrencyRates.COL_CURRENCY_CODE to TEXT,
      CurrencyRates.COL_CURRENCY_NAME to TEXT,
      CurrencyRates.COL_QUANTITY to INTEGER,
      CurrencyRates.COL_PRICE to REAL,
      CurrencyRates.COL_INDEX to TEXT,
      CurrencyRates.COL_CHANGE to REAL
    )
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.dropTable(CurrencyRates.TABLE_NAME, true)
    db.dropTable("CurrencyRatesItem", true)
    onCreate(db)
  }
}
