package com.nbrk.rates.ui.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.ui.common.RatesViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.sdk25.listeners.onItemSelectedListener
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat

/**
 * Created by Roman Shakirov on 21-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ChartFragment : Fragment() {

  private val ratesViewModel by lazy { ViewModelProviders.of(activity!!).get(RatesViewModel::class.java) }
  private val ratesSpinnerAdapter = RatesSpinnerAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_chart, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    spCurrency.apply {
      adapter = ratesSpinnerAdapter
      setSelection(0, false)
      onItemSelectedListener { onItemSelected { _, _, _, _ -> setCurrencyAndPeriod() } }
    }

    val periods = listOf(
      7 to resources.getString(R.string.week),
      30 to resources.getString(R.string.month),
      365 to resources.getString(R.string.year)
    )

    val periodSpinnerAdapter = PeriodSpinnerAdapter().apply { dataSource = periods }

    spPeriod.apply {
      adapter = periodSpinnerAdapter
      setSelection(0, false)
      onItemSelectedListener { onItemSelected { _, _, _, _ -> setCurrencyAndPeriod() } }
    }

    configChart()
    observeLiveData()
  }

  private fun setCurrencyAndPeriod() {
    val currency = (spCurrency.selectedItem as RatesItem).currencyCode
    val period = spPeriod.selectedItem as Int
    ratesViewModel.currencyAndPeriod.value = Pair(currency, period)
  }

  private fun observeLiveData() {
    ratesViewModel.rates.observe(this, Observer<List<RatesItem>> {
      it?.let { ratesSpinnerAdapter.dataSource = it }
    })

    ratesViewModel.chartRates.observe(this, Observer<List<RatesItem>> {
      it?.let { rates ->
        resetXAxisFormatter(rates)
        setChartData(rates)
        chart.invalidate()
      }
    })
  }

  private fun configChart() {

    val yAxisFormatter = IAxisValueFormatter { yValue, _ ->
      val formatter = DecimalFormat("###,###,##0.##")
      formatter.format(yValue)
    }

//    val xAxisFormatter = IAxisValueFormatter { xValue, _ -> "$xValue" }

    val lineLength = 10f
    val spaceLength = 10f
    val phase = 0f

    chart.axisLeft.enableGridDashedLine(lineLength, spaceLength, phase)
    chart.axisLeft.valueFormatter = yAxisFormatter

    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    chart.xAxis.enableGridDashedLine(lineLength, spaceLength, phase)
//    chart.xAxis.valueFormatter = xAxisFormatter

    chart.axisRight.isEnabled = false
    chart.description.isEnabled = false
    chart.setDrawGridBackground(false)
  }

  private fun setChartData(rates: List<RatesItem>) {
    val lineDataSet = prepareLineDataSet(rates)
    chart.data = LineData(lineDataSet)
  }

  private fun prepareLineDataSet(rates: List<RatesItem>): LineDataSet {
    val entries = rates.sortedBy { it.date }
      .mapIndexed { index, ratesItem -> Entry(index.toFloat(), ratesItem.price.toFloat()) }

    val label = with(rates.first()) { "$quantity $currencyName" }

    val lineDataSet = LineDataSet(entries, label).apply {
      axisDependency = YAxis.AxisDependency.LEFT
      setDrawFilled(true)
      lineWidth = 4f
      val formatter = DecimalFormat("###,###,##0.##")
      valueFormatter = IValueFormatter { value, _, _, _ -> formatter.format(value) }
    }

    return lineDataSet
  }

  private fun resetXAxisFormatter(rates: List<RatesItem>) {
    var xAxisFormatter = DateTimeFormatter.ofPattern("dd MMM")
    if (rates.size == 7)
      xAxisFormatter = DateTimeFormatter.ofPattern("EEE")
    val xLabels = rates.sortedBy { it.date }.map { xAxisFormatter.format(it.date) }
    chart.xAxis.valueFormatter = XAxisFormatter(xLabels)
  }

  class XAxisFormatter(private val labels: List<String>) : IAxisValueFormatter {
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
      return labels[value.toInt()]
    }
  }
}