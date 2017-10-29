package com.nbrk.rates

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import com.nbrk.rates.data.local.room.AppDatabase
import org.junit.After
import org.junit.Before

/**
 * Created by Roman Shakirov on 29-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
abstract class DbTest {

  protected lateinit var database: AppDatabase

  @Before
  fun initDb() {
    database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
      AppDatabase::class.java)
      .allowMainThreadQueries()
      .build()
  }

  @After
  fun closeDb() {
    database.close()
  }
}