package com.nbrk.rates.chart.presenter

import android.os.Bundle
import com.nbrk.rates.chart.view.activities.ChartActivity
import com.nbrk.rates.extensions.applySchedulers
import com.nbrk.rates.home.model.RatesModel
import nucleus.presenter.RxPresenter

/**
 * Created by rpagyc on 26-Jan-16.
 */
class ChartPresenter : RxPresenter<ChartActivity>() {
  val LOAD_CURRENCY = 1
  val LOAD_RATES = 2

  var date = ""
  var range = 0
  var currency = ""

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)

    restartableLatestCache(LOAD_CURRENCY,
      { RatesModel.instance.getDbRates(date).applySchedulers() },
      { view, currency -> view.showCurrency(currency) })
    { view, error -> view.showError(error) }

    restartableLatestCache(LOAD_RATES,
      { RatesModel.instance.getRatesItems(date, range, currency).applySchedulers() },
      { view, rates -> view.showRates(rates) })
    { view, error -> view.showError(error) }
  }

  fun loadCurrency(date: String) {
    this.date = date
    start(LOAD_CURRENCY)
  }

  fun loadRates(date: String, range: Int, currency: String) {
    this.date = date
    this.range = range
    this.currency = currency
    start(LOAD_RATES)
  }
}