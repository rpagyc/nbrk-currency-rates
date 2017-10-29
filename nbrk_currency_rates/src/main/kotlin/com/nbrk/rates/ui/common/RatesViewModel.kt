package com.nbrk.rates.ui.common

import android.app.Application
import android.arch.lifecycle.*
import com.nbrk.rates.base.BaseApplication
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.util.toLiveData
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesViewModel(app: Application = BaseApplication.INSTANCE,
                     private val repository: RatesRepository = RatesRepository())
  : AndroidViewModel(app) {

//  private val repository: RatesRepository = RatesRepository()

  val date = MutableLiveData<Date>()

  val rates: LiveData<List<RatesItem>> = Transformations
    .switchMap(date) {
      isLoading.value = true
      repository.getRates(it).toLiveData()
    }

  val isLoading = MediatorLiveData<Boolean>().apply { addSource(rates) { value = false } }

  val currencyAndPeriod = MutableLiveData<Pair<String, Int>>()

  val chartRates: LiveData<List<RatesItem>> = Transformations.switchMap(currencyAndPeriod) {
    repository.getChartRates(it.first, it.second).toLiveData()
  }

  fun refresh() {
    date.value = Calendar.getInstance().time
  }
}