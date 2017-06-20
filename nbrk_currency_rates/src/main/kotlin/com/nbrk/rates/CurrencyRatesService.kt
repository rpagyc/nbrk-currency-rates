package com.nbrk.rates

import android.app.IntentService
import android.content.Intent
import com.nbrk.rates.extensions.TAG
import com.nbrk.rates.extensions.debug
import com.nbrk.rates.extensions.error
import com.nbrk.rates.home.model.RatesModel
import java.util.*

class CurrencyRatesService : IntentService(TAG()) {
  companion object {
    val KEY_DATE = "${TAG()}#date"
    val KEY_PERIOD = "${TAG()}#period"
    val KEY_CURRENCY = "${TAG()}#currency"
  }

  var date = ""
  var period = 0
  var currency = ""

  override fun onHandleIntent(intent: Intent) {

    date = intent.getStringExtra(KEY_DATE)
    period = intent.getIntExtra(KEY_PERIOD, 0)
    currency = intent.getStringExtra(KEY_CURRENCY)?:""

    if (period > 0) {
      loadRatesForPeriod()
    } else {
      loadRatesForDate()
    }

  }

  fun loadRatesForDate() {
    RatesModel.instance.getRestRates(date)
      .subscribe(
        { rates -> debug(rates) },
        { error -> error(error) }
      )
  }

  fun loadRatesForPeriod() {
    val startDate = Calendar.getInstance()
    startDate.add(Calendar.DATE, -period)

//    RatesModel.instance.getRatesItems(date, period, currency)
//      .take(1)
//      .filter { it.size < period }
//      .map { it.map { it.date?.toDateString(SimpleDateFormat("dd.MM.yyyy")) } }
//      .flatMap {
//        dbDates ->
//        Observable.from(1..period)
//          .filter {
//            startDate.add(Calendar.DATE, 1)
//            startDate.toDateString() !in dbDates
//          }
//      }
//      .subscribe(
//        { dataSource -> debug(dataSource) },
//        { error -> error(error) }
//      )
  }

}