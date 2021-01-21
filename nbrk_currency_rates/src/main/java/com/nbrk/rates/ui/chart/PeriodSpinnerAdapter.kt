package com.nbrk.rates.ui.chart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nbrk.rates.R
import com.nbrk.rates.databinding.SpinnerItemPeriodBinding

/**
 * Created by Roman Shakirov on 10-Nov-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
 
class PeriodSpinnerAdapter : BaseAdapter() {
  var dataSource : List<Pair<Int, String>> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val binding = SpinnerItemPeriodBinding.inflate(LayoutInflater.from(parent.context))
    binding.label.text = dataSource[position].second
    return binding.root
  }

  override fun getItem(position: Int): Any {
    return dataSource[position].first
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return dataSource.size
  }
}