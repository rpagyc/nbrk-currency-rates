package com.nbrk.rates.ui.rates

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.nbrk.rates.Injection
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : Fragment() {

  private val viewModelFactory by lazy { Injection.provideViewModelFactory(activity!!) }
  private val ratesViewModel by lazy {
    ViewModelProviders.of(activity!!, viewModelFactory).get(RatesViewModel::class.java)
  }

  private val title by lazy { resources.getString(R.string.last_updated) }
  private val adapter = RatesAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_rates, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    ratesList.setHasFixedSize(true)
    ratesList.adapter = adapter

    lRefresh.setOnRefreshListener {
      activity?.title = getString(R.string.rates)
      ratesViewModel.refresh()
    }

    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    adView.loadAd(adRequest)

//    ratesViewModel.refresh()

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

    val formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")
    ratesViewModel.date.observe(this, Observer<LocalDate> {
      it?.let { tvTitle.text = "$title ${LocalDateTime.now().format(formatter)}" }
    })

  }

}