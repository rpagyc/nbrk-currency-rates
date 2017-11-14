package com.nbrk.rates.ui.chart

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.nbrk.rates.Injection
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.ui.common.RatesViewModel
import kotlinx.android.synthetic.main.fragment_chart.*
import org.jetbrains.anko.sdk25.listeners.onItemSelectedListener
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.DecimalFormat

/**
 * Created by Roman Shakirov on 21-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class ChartFragment : Fragment() {

  private val viewModelFactory by lazy { Injection.provideViewModelFactory(activity!!) }
  private val ratesViewModel by lazy {
    ViewModelProviders.of(activity!!, viewModelFactory).get(RatesViewModel::class.java)
  }

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
        if (rates.isNotEmpty()) {
          setChartData(rates)
          chart.invalidate()
        }
      }
    })
  }

  private fun configChart() {

    val yAxisFormatter = IAxisValueFormatter { yValue, _ ->
      val formatter = DecimalFormat("###,###,##0.##")
      formatter.format(yValue)
    }

    val xAxisFormatter = IAxisValueFormatter { xValue, _ ->
      var formatter = DateTimeFormatter.ofPattern("dd MMM")
      val lineDataSet = chart.data.getDataSetByIndex(0) as LineDataSet
      val entriesCount = lineDataSet.values.size
      if (entriesCount == 7)
        formatter = DateTimeFormatter.ofPattern("EEE")
      var label = ""
      if (entriesCount > xValue.toInt())
      {
        val date = lineDataSet.values[xValue.toInt()].data as LocalDate
        label = date.format(formatter)
      }
      label
    }

    val lineLength = 10f
    val spaceLength = 10f
    val phase = 0f

    chart.apply {
      axisLeft.enableGridDashedLine(lineLength, spaceLength, phase)
      axisLeft.valueFormatter = yAxisFormatter

      xAxis.position = XAxis.XAxisPosition.BOTTOM
      xAxis.enableGridDashedLine(lineLength, spaceLength, phase)
      xAxis.valueFormatter = xAxisFormatter

      axisRight.isEnabled = false
      description.isEnabled = false
      setDrawGridBackground(false)
    }
  }

  private fun setChartData(rates: List<RatesItem>) {
    val entries: List<Entry> = rates.sortedBy { it.date }
      .mapIndexed { index, ratesItem -> Entry(index.toFloat(), ratesItem.price.toFloat(), ratesItem.date) }
    val label = rates.firstOrNull()?.let { "${it.quantity} ${it.currencyName}" }
    var lineDataSet: LineDataSet
    if (chart.data != null && chart.data.dataSetCount > 0) {
      lineDataSet = chart.data.getDataSetByIndex(0) as LineDataSet
      lineDataSet.values = entries
      chart.data.notifyDataChanged()
      chart.notifyDataSetChanged()
    } else {
      lineDataSet = LineDataSet(entries, label).apply {
        valueTextSize = 10f
        axisDependency = YAxis.AxisDependency.LEFT
        setDrawFilled(true)
        lineWidth = 4f
        val formatter = DecimalFormat("###,###,##0.##")
        valueFormatter = IValueFormatter { value, _, _, _ -> formatter.format(value) }
      }
      chart.data = LineData(lineDataSet)
    }
  }
}

