package com.nbrk.rates.ui.common

import androidx.lifecycle.*
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.util.toLiveData
import org.threeten.bp.LocalDate

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesViewModel(private val repository: RatesRepository)
  : ViewModel() {

  val date = MutableLiveData<LocalDate>()

  val rates: LiveData<List<RatesItem>> = Transformations.switchMap(date) {
    isLoading.value = true
    repository.getAppRates(it).toLiveData()
  }

  val isLoading = MediatorLiveData<Boolean>().apply { addSource(rates) { value = false } }

  val currencyAndPeriod = MutableLiveData<Pair<String, Int>>()

  val chartRates: LiveData<List<RatesItem>> = Transformations.switchMap(currencyAndPeriod) {
    repository.getChartRates(it.first, it.second).toLiveData()
  }

  fun refresh() {
    date.value = LocalDate.now()
  }

}