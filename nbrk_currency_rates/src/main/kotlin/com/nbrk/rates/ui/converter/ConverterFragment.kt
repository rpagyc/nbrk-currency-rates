package com.nbrk.rates.ui.converter

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.ui.common.RatesViewModel
import kotlinx.android.synthetic.main.fragment_converter.*
import org.jetbrains.anko.sdk25.listeners.onItemSelectedListener
import org.jetbrains.anko.sdk25.listeners.textChangedListener
import org.threeten.bp.LocalDate

/**
 * Created by Roman Shakirov on 18-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ConverterFragment : Fragment() {

  private val ratesViewModel by lazy { ViewModelProviders.of(activity!!).get(RatesViewModel::class.java) }
  private val ratesSpinnerAdapter = RatesSpinnerAdapter()
  private var rates = mutableListOf<RatesItem>()
  private val kzt = RatesItem(
    currencyCode = "KZT",
    currencyName = "ТЕНГЕ",
    price = 1.0,
    quantity = 1,
    date = LocalDate.now(),
    index = "",
    change = 0.0
  )

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_converter, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    spFromCurrency.apply {
      adapter = ratesSpinnerAdapter
      onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    }
    spToCurrency.apply {
      adapter = ratesSpinnerAdapter
      onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    }
    etFromAmount.apply {
      setText("1.00")
      textChangedListener { onTextChanged { _, _, _, _ -> convert() } }
    }

    observeLiveData()
  }

  private fun observeLiveData() {
    ratesViewModel.rates.observe(this, Observer<List<RatesItem>> {
      it?.let {
        rates = it.toMutableList()
        rates.add(kzt)
        ratesSpinnerAdapter.dataSource = rates
      }
    })
  }

  private fun convert() {
    if (rates.isNotEmpty()) {
      val fromCurrency = spFromCurrency.selectedItem as RatesItem
      val toCurrency = spToCurrency.selectedItem as RatesItem

      val fromPrice = fromCurrency.price
      val fromQuant = fromCurrency.quantity

      val toPrice = toCurrency.price
      val toQuant = toCurrency.quantity

      if (etFromAmount.text.toString().isNotBlank()) {
        val amount = etFromAmount.text.toString().toDouble()
        etToAmount.setText("%.2f".format(amount * (fromPrice / fromQuant) / (toPrice / toQuant)))
      } else {
        etToAmount.setText("")
      }
    }
  }
}