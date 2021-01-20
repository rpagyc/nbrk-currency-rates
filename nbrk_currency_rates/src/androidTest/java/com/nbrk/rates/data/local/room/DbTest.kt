package com.nbrk.rates.data.local.room

import androidx.room.Room
import androidx.test.InstrumentationRegistry
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