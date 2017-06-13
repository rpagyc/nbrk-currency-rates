package com.nbrk.rates.rates

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.nbrk.rates.R
import com.nbrk.rates.base.BaseLifecycleActivity
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.extensions.toDateString
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesActivity : BaseLifecycleActivity<RatesViewModel>()
  , SwipeRefreshLayout.OnRefreshListener {

  override val viewModelClass = RatesViewModel::class.java
  private val rv by lazy { findViewById(R.id.rv) as RecyclerView }
  private val vRefresh by lazy { findViewById(R.id.lRefresh) as SwipeRefreshLayout }
  private val adapter = RatesAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_rates)
    rv.setHasFixedSize(true)
    rv.adapter = adapter
    vRefresh.setOnRefreshListener(this)

    if (savedInstanceState == null) {
      viewModel.setDate(Calendar.getInstance().toDateString())
    }
    observeLiveData()
  }

  private fun observeLiveData() {
    viewModel.isLoadingLiveData.observe(this, Observer<Boolean> {
      it?.let { vRefresh.isRefreshing = it }
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