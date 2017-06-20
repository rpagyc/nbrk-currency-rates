package com.nbrk.rates.converter

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbrk.rates.R
import com.nbrk.rates.converter.view.adapters.RatesSpinnerAdapter
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.rates.RatesViewModel
import kotlinx.android.synthetic.main.fragment_converter.*
import org.jetbrains.anko.onItemSelectedListener
import org.jetbrains.anko.textChangedListener
import java.util.*

/**
 * Created by Roman Shakirov on 18-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ConverterFragment : LifecycleFragment() {

  private val ratesViewModel by lazy { ViewModelProviders.of(activity).get(RatesViewModel::class.java) }
  private val adapter = RatesSpinnerAdapter()
  private var rates = ArrayList<RatesItem>()
  private val kzt = RatesItem(
    currencyCode = "KZT",
    currencyName = "ТЕНГЕ",
    price = 1.0,
    quantity = 1
  )

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_converter, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    spFromCurrency.adapter = adapter
    spToCurrency.adapter = adapter
    spFromCurrency.onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    spToCurrency.onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    etFromAmount.setText("1.00")
    etFromAmount.textChangedListener {
      onTextChanged { _, _, _, _ -> convert() }
    }
    observeLiveData()
  }

  fun observeLiveData() {
    ratesViewModel.ratesLiveData.observe(this, Observer<Rates> {
      it?.let {
        rates = it.rates as ArrayList<RatesItem>
        rates.add(kzt)
        adapter.dataSource = it.rates
      }
    })
  }

  fun convert() {
    if (rates.isNotEmpty()) {
      val price1 = rates[spFromCurrency.selectedItemPosition].price
      val quant1 = rates[spFromCurrency.selectedItemPosition].quantity
      val price2 = rates[spToCurrency.selectedItemPosition].price
      val quant2 = rates[spToCurrency.selectedItemPosition].quantity

      if (etFromAmount.text.toString().isNotBlank()) {
        val amount = etFromAmount.text.toString().toDouble()
        etToAmount.setText("${"%.2f".format(amount * (price1 / quant1) / (price2 / quant2))}")
      } else {
        etToAmount.setText("")
      }
    }
  }
}