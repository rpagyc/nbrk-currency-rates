package com.nbrk.rates.rates

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.nbrk.rates.R
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.extensions.debug
import com.nbrk.rates.extensions.toDateString
import kotlinx.android.synthetic.main.fragment_rates.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : LifecycleFragment() {

  private val viewModel by lazy { ViewModelProviders.of(activity).get(RatesViewModel::class.java) }
  private val title by lazy { resources.getString(R.string.last_updated) }
  private val adapter = RatesAdapter()

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_rates, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv.setHasFixedSize(true)
    rv.adapter = adapter

    lRefresh.setOnRefreshListener { viewModel.setDate(Calendar.getInstance().time) }
    if (savedInstanceState == null) {
      viewModel.setDate(Calendar.getInstance().time)
    }

    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    adView.loadAd(adRequest)

    observeLiveData()
  }

  fun observeLiveData() {
    viewModel.isLoadingLiveData.observe(this, Observer<Boolean> {
      it?.let { lRefresh.isRefreshing = it }
    })

    viewModel.ratesLiveData.observe(this, Observer<Rates> {
      it?.let {
        adapter.dataSource = it.rates
      }
    })

    viewModel.throwableLiveData.observe(this, Observer<Throwable> {
      it?.let {
        //Snackbar.make(rv, it.localizedMessage, Snackbar.LENGTH_LONG).show()
        debug(it.localizedMessage)
      }
    })

    viewModel.dateLiveData.observe(this, Observer<Date> {
      it?.let {
        tvTitle.text = "$title ${it.toDateString(SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()))}"
      }
    })
  }

}