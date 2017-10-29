package com.nbrk.rates.ui.rates

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesViewModel
import com.nbrk.rates.util.toDateString
import kotlinx.android.synthetic.main.fragment_rates.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : Fragment() {

  private val ratesViewModel by lazy {
    ViewModelProviders.of(activity!!).get(RatesViewModel::class.java)
  }
  private val title by lazy { resources.getString(R.string.last_updated) }
  private val adapter = RatesAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_rates, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    rv.setHasFixedSize(true)
    rv.adapter = adapter

    lRefresh.setOnRefreshListener { ratesViewModel.refresh() }

    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    adView.loadAd(adRequest)

    ratesViewModel.refresh()

    observeLiveData()
  }

  private fun observeLiveData() {

    ratesViewModel.rates.observe(this, Observer<List<RatesItem>> {
      it?.let {
        adapter.dataSource = it
      }
    })

    ratesViewModel.isLoading.observe(this, Observer<Boolean> {
      it?.let { lRefresh.isRefreshing = it }
    })

    ratesViewModel.date.observe(this, Observer<Date> {
      it?.let { tvTitle.text = "$title ${it.toDateString(SimpleDateFormat("dd MMM HH:mm",
        Locale.getDefault()))}" }
    })

  }

}