package com.nbrk.rates.chart

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.preference.PreferenceManager
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.RatesItem

/**
 * Created by Roman Shakirov on 25-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ChartViewModel(application: Application?) : AndroidViewModel(application) {

  private val ratesRepository = RatesRepository(PreferenceManager.getDefaultSharedPreferences(application))

  val currencyAndPeriod = MutableLiveData<Pair<String, Int>>()

  private val resultLiveData = ChartLiveData(ratesRepository)
  init {
    resultLiveData.addSource(currencyAndPeriod) {
      it?.let { resultLiveData.currencyAndPeriod = it }
    }
  }

  val chartLiveData = MediatorLiveData<List<RatesItem>>()
  init {
    chartLiveData.addSource(resultLiveData) {
      it?.first?.let { chartLiveData.value = it }
    }
  }

  val throwableLiveData = MediatorLiveData<Throwable>()
  init {
    throwableLiveData.addSource(resultLiveData) {
      it?.second?.let { throwableLiveData.value = it }
    }
  }

  fun setCurrencyAndPeriod(currencyAndPeriod: Pair<String, Int>){
    this.currencyAndPeriod.value = currencyAndPeriod
  }
}