package com.nbrk.rates.converter.view.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.nbrk.rates.R
import com.nbrk.rates.base.ToolbarManager
import com.nbrk.rates.converter.presenter.ConverterPresenter
import com.nbrk.rates.converter.view.adapters.RatesSpinnerAdapter
import com.nbrk.rates.entities.Rates
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.error
import com.nbrk.rates.extensions.toDateString
import kotlinx.android.synthetic.main.activity_converter.*
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.onItemSelectedListener
import org.jetbrains.anko.textChangedListener
import java.util.*

@RequiresPresenter(ConverterPresenter::class)
class ConverterActivity : NucleusActivity<ConverterPresenter>(), ToolbarManager {

  var rates = ArrayList<RatesItem>()

  val kzt = RatesItem(
    id = 0,
    date = Calendar.getInstance().time,
    currencyCode = "KZT",
    currencyName = "ТЕНГЕ",
    price = 1.00,
    quantity = 1,
    index = "",
    change = 0.00
  )

  val date = Calendar.getInstance().toDateString()

  override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  val spinnerAdapter by lazy { RatesSpinnerAdapter() }

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_converter)

    toolbarTitle = getString(R.string.conversion_title)
    enableHomeAsUp { onBackPressed() }

    etFromAmount.setText("1.00")
    etFromAmount.textChangedListener {
      onTextChanged { charSequence, i1, i2, i3 -> convert() }
    }

    spFromCurrency.adapter = spinnerAdapter
    spFromCurrency.onItemSelectedListener {
      onItemSelected { adapterView, view, i, l -> convert() }
    }

    spToCurrency.adapter = spinnerAdapter
    spToCurrency.onItemSelectedListener {
      onItemSelected { adapterView, view, i, l -> convert() }
    }

    if (savedInstanceState == null) {
      presenter.loadRates(date)
    }
  }

  fun convert() {
    if (rates.isNotEmpty()) {
      val price1 = rates[spFromCurrency.selectedItemPosition].price
      val quant1 = rates[spFromCurrency.selectedItemPosition].quantity
      val price2 = rates[spToCurrency.selectedItemPosition].price
      val quant2 = rates[spToCurrency.selectedItemPosition].quantity

      if (etFromAmount.text.toString().isNotBlank()) {
        val amount = etFromAmount.text.toString().toDouble()
        etToAmount.setText("%.2f".format(amount * (price1 / quant1) / (price2 / quant2))
          .replace(",", "."))
      } else {
        etToAmount.setText("")
      }
    }
  }

  fun showRates(rates: Rates) {
    this.rates = rates.rates as ArrayList<RatesItem>
    this.rates.add(kzt)
    spinnerAdapter.dataSource = rates.rates
    spToCurrency.setSelection(this.rates.size - 1)
  }

  fun showError(error: Throwable) {
    error(error.message)
  }

}
