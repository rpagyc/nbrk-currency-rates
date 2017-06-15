package com.nbrk.rates

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.extensions.toDateString
import com.nbrk.rates.rates.RatesAdapter
import com.nbrk.rates.rates.RatesViewModel
import kotlinx.android.synthetic.main.fragment_rates.*
import java.util.*

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesFragment : LifecycleFragment(), SwipeRefreshLayout.OnRefreshListener {

  companion object {
    val TAG = "RatesViewModel"
  }
  
  private val viewModel by lazy {  ViewModelProviders.of(this).get(RatesViewModel::class.java) }
  private val adapter = RatesAdapter()

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_rates, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    rv.setHasFixedSize(true)
    rv.adapter = adapter
    lRefresh.setOnRefreshListener(this)
    viewModel.setDate(Calendar.getInstance().toDateString())
    observeLiveData()
  }

  fun observeLiveData() {
    viewModel.isLoadingLiveData.observe(this, Observer<Boolean> {
      it?.let { lRefresh.isRefreshing = it }
    })

    viewModel.ratesLiveData.observe(this, Observer<Rates> {
      it?.let { adapter.dataSource = it.rates }
    })

    viewModel.throwableLiveData.observe(this, Observer<Throwable> {
      it?.let { Snackbar.make(rv, it.localizedMessage, Snackbar.LENGTH_LONG).show() }
    })
  }

  override fun onRefresh() {
    viewModel.setDate(Calendar.getInstance().toDateString())
  }

}