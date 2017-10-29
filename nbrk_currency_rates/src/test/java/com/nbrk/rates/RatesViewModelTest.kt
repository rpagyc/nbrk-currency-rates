package com.nbrk.rates

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nbrk.rates.data.local.room.dao.RatesDao
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * Created by Roman Shakirov on 29-Oct-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
class RatesViewModelTest {
  @get:Rule
  var instantTaskExecutorRule = InstantTaskExecutorRule()

  @Mock
  private lateinit var ratesDao: RatesDao

  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  fun fakeTest() {}
}