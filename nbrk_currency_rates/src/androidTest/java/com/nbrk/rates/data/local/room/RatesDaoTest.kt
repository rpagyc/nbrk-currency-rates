package com.nbrk.rates.data.local.room

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.support.test.runner.AndroidJUnit4
import com.nbrk.rates.data.local.domain.model.RatesItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.threeten.bp.LocalDate

/**
 * Created by Roman Shakirov on 23-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */

@RunWith(AndroidJUnit4::class)
class RatesDaoTest : DbTest() {

  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  private val date = LocalDate.now()

  private val kzt = RatesItem(
    currencyCode = "KZT",
    currencyName = "ТЕНГЕ",
    price = 1.0,
    quantity = 1,
    date = date,
    index = "",
    change = 0.0
  )

  @Test
  fun getEmptyListOfRates() {
    database.ratesDao()
      .getRates(date)
      .test()
      .assertValue { it.isEmpty() }
  }

  @Test
  fun insertAndGetRates() {
    database.ratesDao()
      .saveRates(emptyList())

    database.ratesDao()
      .getRates(date)
      .test()
      .assertValue { it.isNotEmpty() }
  }

}