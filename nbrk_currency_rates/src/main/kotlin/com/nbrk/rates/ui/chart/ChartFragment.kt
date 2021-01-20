package com.nbrk.rates.ui.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IValueFormatter
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.databinding.FragmentChartBinding
import com.nbrk.rates.ui.common.RatesSpinnerAdapter
import com.nbrk.rates.ui.common.RatesViewModel
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

  private var _binding: FragmentChartBinding? = null
  private val binding get() = _binding!!

  private val ratesViewModel: RatesViewModel by activityViewModels()

  private val ratesSpinnerAdapter = RatesSpinnerAdapter()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = FragmentChartBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.spCurrency.apply {
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

    binding.spPeriod.apply {
      adapter = periodSpinnerAdapter
      setSelection(0, false)
      onItemSelectedListener { onItemSelected { _, _, _, _ -> setCurrencyAndPeriod() } }
    }

    configChart()
    observeLiveData()
  }

  private fun setCurrencyAndPeriod() {
    val currency = (binding.spCurrency.selectedItem as RatesItem).currencyCode
    val period = binding.spPeriod.selectedItem as Int
    ratesViewModel.currencyAndPeriod.value = Pair(currency, period)
  }

  private fun observeLiveData() {
    ratesViewModel.rates.observe(viewLifecycleOwner, Observer<List<RatesItem>> {
      it?.let { ratesSpinnerAdapter.dataSource = it }
    })

    ratesViewModel.chartRates.observe(viewLifecycleOwner, Observer<List<RatesItem>> {
      it?.let { rates ->
        if (rates.isNotEmpty()) {
          setChartData(rates)
          binding.chart.invalidate()
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
      val lineDataSet = binding.chart.data.getDataSetByIndex(0) as LineDataSet
      val entriesCount = lineDataSet.values.size
      val formatter = if (entriesCount == 7) {
        DateTimeFormatter.ofPattern("EEE")
      } else {
        DateTimeFormatter.ofPattern("dd MMM")
      }
      var label = ""
      if (entriesCount > xValue.toInt()) {
        val date = lineDataSet.values[xValue.toInt()].data as LocalDate
        label = date.format(formatter)
      }
      label
    }

    val lineLength = 10f
    val spaceLength = 10f
    val phase = 0f

    binding.chart.apply {
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
    val lineDataSet = LineDataSet(entries, label).apply {
      if (entries.size == 7) {
        valueTextSize = 10f
        circleRadius = 6f
      }
      lineWidth = 4f
      axisDependency = YAxis.AxisDependency.LEFT
      val formatter = DecimalFormat("###,###,##0.##")
      valueFormatter = IValueFormatter { value, _, _, _ -> formatter.format(value) }
      setDrawFilled(true)
    }
    binding.chart.data = LineData(lineDataSet)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}

