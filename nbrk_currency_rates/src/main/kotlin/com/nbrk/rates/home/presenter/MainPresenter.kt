package com.nbrk.rates.home.presenter

import android.os.Bundle
import com.nbrk.rates.extensions.applySchedulers
import com.nbrk.rates.home.model.RatesModel
import com.nbrk.rates.home.view.activities.MainActivityOld
import nucleus.presenter.RxPresenter

/**
 * Created by rpagyc on 15-Jan-16.
 */
class MainPresenter : RxPresenter<MainActivityOld>() {

  var date = ""
  val LOAD_RATES = 1

  override fun onCreate(savedState: Bundle?) {
    super.onCreate(savedState)
    restartableLatestCache(LOAD_RATES,
      { RatesModel.instance.getDbRates(date).applySchedulers() },
      { view, rates -> view.showRates(rates) })
    { view, error -> view.showError(error) }
  }

  fun loadRates(date: String) {
    this.date = date
    start(LOAD_RATES)
  }
}