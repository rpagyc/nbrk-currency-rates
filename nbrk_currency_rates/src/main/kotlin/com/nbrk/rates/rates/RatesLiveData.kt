package com.nbrk.rates.rates

import android.arch.lifecycle.MediatorLiveData
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.Rates
import io.reactivex.disposables.Disposable

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesLiveData(val repository: RatesRepository)
  : MediatorLiveData<Pair<Rates?, Throwable?>>() {

  private var disposable: Disposable? = null

  var date: String? = null
    set(value) {
      value?.let {
        disposable = repository
          .getRates(it)
          .subscribe { data, error -> this@RatesLiveData.value = Pair(data, error) }
      }
    }

}