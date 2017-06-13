package com.nbrk.rates.home.view.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.widget.DatePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.nbrk.rates.App
import com.nbrk.rates.CurrencyRatesService
import com.nbrk.rates.R
import com.nbrk.rates.base.ToolbarManager
import com.nbrk.rates.chart.view.activities.ChartActivity
import com.nbrk.rates.converter.view.activities.ConverterActivity
import com.nbrk.rates.extensions.error
import com.nbrk.rates.extensions.toDateString
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.home.presenter.MainPresenter
import com.nbrk.rates.home.view.adapters.RatesListAdapter
import com.nbrk.rates.settings.view.activities.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startService
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

@RequiresPresenter(MainPresenter::class)
class MainActivity : NucleusActivity<MainPresenter>(), ToolbarManager {

  var date = Calendar.getInstance()
  var sTitle = ""
  val sdf2 = SimpleDateFormat("dd MMM yyyy (HH:mm)")

  val onDateSet = fun(datePicker: DatePicker, year: Int, month: Int, day: Int) {
    date.set(year, month, day)
    loadRates()
  }

  override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  override fun initToolbar() {
    toolbar.inflateMenu(R.menu.main_menu)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.menu_refresh -> {
          //reset date to Today
          date = Calendar.getInstance()
          loadRates()
        }
        R.id.menu_date -> DatePickerDialog(this, onDateSet,
          date.get(Calendar.YEAR),
          date.get(Calendar.MONTH),
          date.get(Calendar.DAY_OF_MONTH)).show()
        R.id.menu_converter -> startActivity<ConverterActivity>()
        R.id.menu_chart -> startActivity<ChartActivity>()
        R.id.menu_settings -> startActivity<SettingsActivity>()
        else -> App.instance.toast("Unknown option")
      }
      true
    }
  }

  val ratesListAdapter by lazy { RatesListAdapter() }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initToolbar()
    toolbarTitle = getString(R.string.app_name)

    refresh.onRefresh {
      //reset date to Today
      date = Calendar.getInstance()
      loadRates()
    }

    sTitle = resources.getString(R.string.last_updated)

    rvRatesList.layoutManager = LinearLayoutManager(this)
    rvRatesList.adapter = ratesListAdapter

    val mAdView = findViewById(R.id.adView) as AdView
    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    mAdView.loadAd(adRequest)

    if (savedInstanceState == null) {
      loadRates()
    }

  }

  fun loadRates() {
    //show progress bar
    refresh.isRefreshing = true
    //start service
    startService<CurrencyRatesService>(CurrencyRatesService.KEY_DATE to date.toDateString())
    //load from db
    presenter.loadRates(date.toDateString())
  }

  fun showRates(rates: Rates) {
    refresh.isRefreshing = false
    tvTitle.text = "$sTitle ${date.toDateString(sdf2)}"
    ratesListAdapter.setData(rates)
  }

  fun showError(error: Throwable) {
    refresh.isRefreshing = false
    error(error.message)
  }

}