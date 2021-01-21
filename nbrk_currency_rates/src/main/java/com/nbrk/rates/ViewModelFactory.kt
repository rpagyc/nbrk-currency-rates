package com.nbrk.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.ui.common.RatesViewModel

/**
 * Created by Roman Shakirov on 03-Nov-17.
 * DigitTonic Studio
 * support@digittonic.com
 */

class ViewModelFactory(private val repository: RatesRepository) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(RatesViewModel::class.java)) {
      return RatesViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}