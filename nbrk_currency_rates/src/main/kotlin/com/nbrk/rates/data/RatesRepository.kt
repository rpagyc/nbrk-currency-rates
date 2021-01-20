package com.nbrk.rates.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.data.local.room.AppDatabase
import com.nbrk.rates.data.local.sharedpref.AppSettings
import com.nbrk.rates.data.remote.rest.RestApi
import com.nbrk.rates.util.error
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Roman Shakirov on 24-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesRepository(context: Context) {

  private val appSettings = AppSettings(context)
  private val remote = RestApi.getInstance()
  private val local = AppDatabase.getInstance(context).ratesDao()

  private val mapper = Mapper()

  fun getChartRates(currency: String, period: Int): Flowable<List<RatesItem>> {
    return Flowable.range(1, period)
      .parallel(period)
      .runOn(Schedulers.io())
      .map { LocalDate.now().minusDays(it.toLong()) }
      .flatMap { getRates(it) }
      .map { it.filter { it.currencyCode == currency } }
      .filter { it.isNotEmpty() }
      .sequential()
      .buffer(period)
      .map { it.flatten() }
  }

  private fun getRatesFromLocal(date: LocalDate): Flowable<List<RatesItem>> {
    return local.getRates(date)
      .distinctUntilChanged()
      .map { mapper.roomRatesToDomain(it) }
  }

  private fun fetchRates(date: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    remote.getRates(date.format(formatter))
      .map { mapper.restRatesToRoom(it) }
      .doOnSuccess { local.saveRates(it) }
      .onErrorReturn {
        error(it.message)
        emptyList()
      }
      .subscribe()
  }

  fun getAppRates(date: LocalDate): Flowable<List<RatesItem>> {
    return getRates(date).map { it.filter { appSettings.isVisibleInApp(it.currencyCode) } }
  }

  fun getWidgetRates(): Flowable<List<RatesItem>> {
    val date = LocalDate.now()
    return getRates(date).map { it.filter { appSettings.isVisibleInWidget(it.currencyCode) } }
  }

  private fun getRates(date: LocalDate): Flowable<List<RatesItem>> {
    return getRatesFromLocal(date)
      .doOnNext { rates ->
        if (rates.isEmpty()) fetchRates(date)
      }
  }

}