package com.nbrk.rates.ui.rates

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.ads.AdRequest
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesViewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : Fragment(R.layout.fragment_rates) {

  private val ratesViewModel by lazy {
    ViewModelProviders.of(requireActivity()).get(RatesViewModel::class.java)
  }

  private val title by lazy { resources.getString(R.string.last_updated) }
  private val adapter = RatesAdapter()
  private var ratesFragmentBinding: RatesFragmentBinding? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = RatesFragmentBinding.bind(view)
    ratesFragmentBinding = binding

    binding.ratesList.setHasFixedSize(true)
    binding.ratesList.adapter = adapter

    binding.lRefresh.setOnRefreshListener {
      activity?.title = getString(R.string.rates)
      ratesViewModel.refresh()
    }

    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    binding.adView.loadAd(adRequest)

//    ratesViewModel.refresh()

    observeLiveData()
  }

  private fun observeLiveData() {

    ratesViewModel.rates.observe(viewLifecycleOwner, Observer<List<RatesItem>> {
      it?.let {
        adapter.dataSource = it
      }
    })

    ratesViewModel.isLoading.observe(viewLifecycleOwner, Observer<Boolean> {
      it?.let { ratesFragmentBinding!!.lRefresh.isRefreshing = it }
    })

    val formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")
    ratesViewModel.date.observe(viewLifecycleOwner, Observer<LocalDate> {
      it?.let { ratesFragmentBinding!!.tvTitle.text = "$title ${LocalDateTime.now().format(formatter)}" }
    })

  }

}