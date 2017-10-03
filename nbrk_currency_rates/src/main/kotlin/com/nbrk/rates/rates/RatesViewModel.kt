package com.nbrk.rates.rates

import android.app.Application
import android.arch.lifecycle.*
import com.nbrk.rates.data.RatesRepository
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.toLiveData
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesViewModel(app: Application) : AndroidViewModel(app) {

  private val repository: RatesRepository = RatesRepository()

  val date = MutableLiveData<Date>()

  val rates: LiveData<List<RatesItem>> =
    Transformations
      .switchMap(date) {
        isLoading.value = true
        repository.getRates(it)
          .map { it.filter { app.defaultSharedPreferences.getBoolean("pref_key_show_${it.currencyCode}", true) }}
          .toLiveData()
      }

  val isLoading = MediatorLiveData<Boolean>().apply { addSource(rates) { value = false } }

  val currencyAndPeriod = MutableLiveData<Pair<String, Int>>()

  val chartRates: LiveData<List<RatesItem>> =
    Transformations.switchMap(currencyAndPeriod) {
      repository.getChartRates(it.first, it.second).toLiveData()
    }
}