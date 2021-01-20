package com.nbrk.rates.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.databinding.FragmentConverterBinding
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.ui.common.RatesViewModel
import org.jetbrains.anko.sdk25.listeners.onItemSelectedListener
import org.jetbrains.anko.sdk25.listeners.textChangedListener
import org.threeten.bp.LocalDate

/**
 * Created by Roman Shakirov on 18-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ConverterFragment : Fragment() {

  private val ratesViewModel: RatesViewModel by activityViewModels()
  
  private var _binding: FragmentConverterBinding? = null
  private val binding get() = _binding!!
  
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
    _binding = FragmentConverterBinding.inflate(inflater, container, false)
    return binding.root
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.spFromCurrency.apply {
      adapter = ratesSpinnerAdapter
      onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    }

    binding.spToCurrency.apply {
      adapter = ratesSpinnerAdapter
      onItemSelectedListener { onItemSelected { _, _, _, _ -> convert() } }
    }

    binding.etFromAmount.apply {
      setText("1.00")
      textChangedListener { onTextChanged { _, _, _, _ -> convert() } }
    }

    observeLiveData()
  }

  private fun observeLiveData() {
    ratesViewModel.rates.observe(viewLifecycleOwner, Observer<List<RatesItem>> {
      it?.let {
        rates = it.toMutableList()
        rates.add(kzt)
        ratesSpinnerAdapter.dataSource = rates
      }
    })
  }

  private fun convert() {
    if (rates.isNotEmpty()) {
      val fromCurrency = binding.spFromCurrency.selectedItem as RatesItem
      val toCurrency = binding.spToCurrency.selectedItem as RatesItem

      val fromPrice = fromCurrency.price
      val fromQuant = fromCurrency.quantity

      val toPrice = toCurrency.price
      val toQuant = toCurrency.quantity

      if (binding.etFromAmount.text.toString().isNotBlank()) {
        val amount = binding.etFromAmount.text.toString().toDouble()
        binding.etToAmount.setText("%.2f".format(amount * (fromPrice / fromQuant) / (toPrice / toQuant)))
      } else {
        binding.etToAmount.setText("")
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}