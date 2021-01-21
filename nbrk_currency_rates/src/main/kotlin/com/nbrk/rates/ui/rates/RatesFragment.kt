package com.nbrk.rates.ui.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.google.android.gms.ads.AdRequest
import com.nbrk.rates.R
import com.nbrk.rates.databinding.FragmentRatesBinding
import com.nbrk.rates.ui.common.RatesViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : Fragment() {

  private val ratesViewModel: RatesViewModel by activityViewModels()

  private var _binding: FragmentRatesBinding? = null
  private val binding get() = _binding!!

  private val title by lazy { resources.getString(R.string.last_updated) }
  private val adapter = RatesAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = FragmentRatesBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.ratesList.setHasFixedSize(true)
    binding.ratesList.adapter = adapter

    binding.lRefresh.setOnRefreshListener {
      activity?.title = getString(R.string.rates)
      ratesViewModel.refresh()
    }

    val adRequest = AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build()
    binding.adView.loadAd(adRequest)

    //ratesViewModel.refresh()

    observeLiveData()
  }

  private fun observeLiveData() {

    ratesViewModel.rates.observe(viewLifecycleOwner, Observer {
      it?.let {
        adapter.dataSource = it
      }
    })

    ratesViewModel.isLoading.observe(viewLifecycleOwner, Observer {
      it?.let { binding.lRefresh.isRefreshing = it }
    })

    val formatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")
    ratesViewModel.date.observe(viewLifecycleOwner, Observer {
      it?.let { binding.tvTitle.text = "$title ${LocalDateTime.now().format(formatter)}" }
    })

  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

}