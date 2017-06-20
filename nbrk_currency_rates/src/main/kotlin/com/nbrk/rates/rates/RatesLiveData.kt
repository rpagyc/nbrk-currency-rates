package com.nbrk.rates.rates

import android.arch.lifecycle.MediatorLiveData
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.Rates
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesLiveData(val repository: RatesRepository)
  : MediatorLiveData<Pair<Rates?, Throwable?>>() {

  private var disposable: Disposable? = null

  var date: Date = Calendar.getInstance().time
    set(value) {
      value.let {
        disposable = repository
          .getRates(it)
          .subscribe { data, error -> this@RatesLiveData.value = Pair(data, error) }
      }
    }

  override fun onInactive() {
    super.onInactive()
    if (disposable?.isDisposed?.not() ?: false) {
      disposable?.dispose()
    }
  }

}