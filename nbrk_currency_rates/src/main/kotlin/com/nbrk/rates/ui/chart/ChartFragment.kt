package com.nbrk.rates.ui.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.nbrk.rates.R
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.sdk25.listeners.onItemSelectedListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Roman Shakirov on 21-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ChartFragment : Fragment() {

  private val ratesViewModel by lazy {
    ViewModelProviders.of(activity!!).get(RatesViewModel::class.java)
  }
  private val adapter = RatesSpinnerAdapter()
  private var currencies = ArrayList<RatesItem>()
  private val periodDays = listOf(7, 30, 365)

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_chart, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    spCurrency.adapter = adapter
    spCurrency.setSelection(0, false)
    spCurrency.onItemSelectedListener { onItemSelected { _, _, _, _ -> load() } }

    val period = resources.getStringArray(R.array.period)

    spPeriod.adapter = ArrayAdapter(activity, R.layout.spinner_item_period, period)
    spPeriod.setSelection(0, false)
    spPeriod.onItemSelectedListener { onItemSelected { _, _, _, _ -> load() } }

    observeLiveData()
  }

  private fun load() {
    if (currencies.size > 0) {
      val currency = currencies[spCurrency.selectedItemPosition].currencyCode
      val period = periodDays[spPeriod.selectedItemPosition]

      ratesViewModel.currencyAndPeriod.value = Pair(currency, period)
    }
  }

  private fun observeLiveData() {
    ratesViewModel.rates.observe(this, Observer<List<RatesItem>> {
      it?.let {
        adapter.dataSource = it
        currencies = it as ArrayList<RatesItem>
      }
    })

    ratesViewModel.chartRates.observe(this, Observer<List<RatesItem>> {
      it?.let {
        showRates(it)
      }
    })
  }

  private fun showRates(rates: List<RatesItem>) {

    val formatter = DecimalFormat("###,###,##0.##")
    val valRates = rates.sortedBy { it.date }
      .mapIndexed { index, ratesItem -> Entry(index.toFloat(), ratesItem.price.toFloat()) }
    val quantity = rates.firstOrNull()?.quantity
    val currency = rates.firstOrNull()?.currencyName
    val legend = "$quantity $currency"
    val setRates = LineDataSet(valRates, legend)
    var sdf = SimpleDateFormat("dd MMM")

    setRates.axisDependency = YAxis.AxisDependency.LEFT
    setRates.setDrawFilled(true)
    setRates.lineWidth = 4f
    setRates.valueFormatter = IValueFormatter { value, _, _, _ -> formatter.format(value) }

    if (rates.size == 7)
      sdf = SimpleDateFormat("EEE")

    chart.axisLeft.enableGridDashedLine(10f, 10f, 0f)
    chart.axisLeft.valueFormatter = IAxisValueFormatter { value, axis -> formatter.format(value) }

    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    chart.xAxis.enableGridDashedLine(10f, 10f, 0f)
    chart.xAxis.valueFormatter = IAxisValueFormatter {
      value, axis -> rates
        .sortedBy { it.date }
        .map { sdf.format(it.date) }[value.toInt()]
    }
    chart.axisRight.isEnabled = false

    chart.setDrawGridBackground(false)
    chart.description.isEnabled = false

    val lineData = LineData(setRates)
    chart.data = lineData
    chart.invalidate() // refresh
  }
}