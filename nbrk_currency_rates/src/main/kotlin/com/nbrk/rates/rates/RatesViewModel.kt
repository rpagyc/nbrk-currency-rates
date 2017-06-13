package com.nbrk.rates.rates

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.Rates

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesViewModel(application: Application?) : AndroidViewModel(application) {

  private val ratesRepository = RatesRepository()

  private val dateLiveData = MutableLiveData<String>()

  val resultLiveData = RatesLiveData(ratesRepository)
  init {
    resultLiveData.addSource(dateLiveData) {
      it?.let { resultLiveData.date = it }
    }
  }

  val isLoadingLiveData = MediatorLiveData<Boolean>()
  init {
    isLoadingLiveData.addSource(resultLiveData) {
      isLoadingLiveData.value = false
    }
  }

  val throwableLiveData = MediatorLiveData<Throwable>()
  init {
    throwableLiveData.addSource(resultLiveData) {
      it?.second?.let { throwableLiveData.value = it }
    }
  }

  val ratesLiveData = MediatorLiveData<Rates>()
  init {
    ratesLiveData.addSource(resultLiveData) {
      it?.first?.let { ratesLiveData.value = it }
    }
  }

  fun setDate(date: String) {
    dateLiveData.value = date
    isLoadingLiveData.value = true
  }
}