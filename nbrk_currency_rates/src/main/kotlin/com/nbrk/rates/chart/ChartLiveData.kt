package com.nbrk.rates.chart

import android.arch.lifecycle.MediatorLiveData
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.RatesItem
import io.reactivex.disposables.Disposable

/**
 * Created by Roman Shakirov on 25-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ChartLiveData(val repository: RatesRepository)
  : MediatorLiveData<Pair<List<RatesItem>, Throwable>>() {

  private var disposable: Disposable? = null

  var currencyAndPeriod = Pair("", 0)
    set(value) {
      value.let {
        disposable = repository
          .getRates(it.first, it.second)
          .subscribe { data, error -> this@ChartLiveData.value = Pair(data, error) }
      }
    }

  override fun onInactive() {
    super.onInactive()
    if (disposable?.isDisposed?.not() ?: false) {
      disposable?.dispose()
    }
  }

}