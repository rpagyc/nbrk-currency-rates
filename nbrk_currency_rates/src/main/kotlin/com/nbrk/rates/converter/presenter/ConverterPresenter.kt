package com.nbrk.rates.converter.presenter

import android.os.Bundle
import com.nbrk.rates.converter.view.activities.ConverterActivity
import com.nbrk.rates.extensions.applySchedulers
import com.nbrk.rates.home.model.RatesModel
import nucleus.presenter.RxPresenter

/**
 * Created by rpagyc on 21-Jan-16.
 */
class ConverterPresenter : RxPresenter<ConverterActivity>() {

  var date = ""
  val LOAD_RATES = 1

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)

    restartableLatestCache(LOAD_RATES,
      { RatesModel.instance.getRates(date).applySchedulers() },
      { view, rates -> view.showRates(rates) })
    { view, error -> view.showError(error) }
  }

  fun loadRates(date: String) {
    this.date = date
    start(LOAD_RATES)
  }
}