package com.nbrk.rates.chart.view.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ArrayAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.formatter.YAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.nbrk.rates.CurrencyRatesService
import com.nbrk.rates.R
import com.nbrk.rates.base.ToolbarManager
import com.nbrk.rates.chart.presenter.ChartPresenter
import com.nbrk.rates.converter.view.adapters.RatesSpinnerAdapter
import com.nbrk.rates.extensions.error
import com.nbrk.rates.extensions.toDateString
import com.nbrk.rates.home.model.entities.Rates
import com.nbrk.rates.home.model.entities.RatesItem
import kotlinx.android.synthetic.main.activity_chart.*
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.onItemSelectedListener
import org.jetbrains.anko.startService
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rpagyc on 29.12.2014.
 */
@RequiresPresenter(ChartPresenter::class)
class ChartActivity : NucleusActivity<ChartPresenter>(),
  ToolbarManager {

  val date = Calendar.getInstance().toDateString()
  var currencyList = ArrayList<RatesItem>()
  val periodDays = listOf(7, 30, 365)

  override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  val spinnerAdapter by lazy { RatesSpinnerAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chart)

    toolbarTitle = getString(R.string.chart_title)
    enableHomeAsUp { onBackPressed() }

    spCurrency.adapter = spinnerAdapter
    spCurrency.onItemSelectedListener {
      onItemSelected { adapterView, view, i, l -> load() }
    }

    val period = resources.getStringArray(R.array.period)

    spPeriod.adapter = ArrayAdapter(this, R.layout.spinner_item_period, period)
    spPeriod.onItemSelectedListener {
      onItemSelected { adapterView, view, i, l -> load() }
    }

    chart.setNoDataText(resources.getString(R.string.loading))

    if (savedInstanceState == null) {
      presenter.loadCurrency(date)
    }

  }

  fun showCurrency(rates: Rates) {
    currencyList = rates.rates.toCollection(arrayListOf<RatesItem>())
    spinnerAdapter.setData(currencyList)
  }

  fun showError(error: Throwable) {
    error(error.message)
  }

  fun showRates(rates: List<RatesItem>) {
    val mFormat = DecimalFormat("###,###,##0.##")

    val valsRates = rates.sortedBy { it.date }
      .mapIndexed { index, ratesItem -> Entry(ratesItem.price.toFloat(), index) }

    var legend = ""
    if (spCurrency.selectedItemPosition >= 0) {
      val selectedCurrency = currencyList[spCurrency.selectedItemPosition]
      legend = "${selectedCurrency.quantity} ${selectedCurrency.currencyName}"
    }

    val setRates = LineDataSet(valsRates, legend)
    setRates.axisDependency = YAxis.AxisDependency.LEFT
    setRates.setDrawFilled(true)
    setRates.lineWidth = 4f
    setRates.valueFormatter = ValueFormatter { value, entry, index, viewPortHandler -> mFormat.format(value) }

    val dataSet = ArrayList<ILineDataSet>();
    dataSet.add(setRates);

    var sdf = SimpleDateFormat("dd MMM")
    if (rates.size == 7)
      sdf = SimpleDateFormat("EEE")

    val xVals = rates.sortedBy { it.date }.map { sdf.format(it.date) }

    val data = LineData(xVals, dataSet);

    chart.axisLeft.enableGridDashedLine(10f, 10f, 0f)
    chart.axisLeft.valueFormatter = YAxisValueFormatter { value, yAxis -> mFormat.format(value) }

    chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
    chart.xAxis.enableGridDashedLine(10f, 10f, 0f)
    chart.axisRight.isEnabled = false

    chart.setDrawGridBackground(false)
    chart.setDescription("")

    chart.data = data;
    chart.invalidate(); // refresh
  }

  fun load() {
    if (currencyList.isNotEmpty()) {
      startService<CurrencyRatesService>(
        CurrencyRatesService.KEY_DATE to date,
        CurrencyRatesService.KEY_PERIOD to periodDays[spPeriod.selectedItemPosition],
        CurrencyRatesService.KEY_CURRENCY to currencyList[spCurrency.selectedItemPosition].currencyCode
      )

      presenter.loadRates(date,
        periodDays[spPeriod.selectedItemPosition],
        currencyList[spCurrency.selectedItemPosition].currencyCode)
    }
  }
}