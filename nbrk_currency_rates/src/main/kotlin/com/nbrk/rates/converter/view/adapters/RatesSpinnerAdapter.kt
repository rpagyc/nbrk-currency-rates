package com.nbrk.rates.converter.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nbrk.rates.R
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.getDrawable
import kotlinx.android.synthetic.main.spinner_item_rates.view.*

/**
 * Created by rpagyc on 25-Jan-16.
 */
class RatesSpinnerAdapter : BaseAdapter() {

  var dataSource : List<RatesItem> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item_rates, parent, false)
    itemView.flag.setImageResource(dataSource[position].currencyCode.getDrawable())
    itemView.tvCurrencyCode.text = dataSource[position].currencyCode
    return itemView
  }

  override fun getItem(position: Int): Any {
    return dataSource[position]
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun getCount(): Int {
    return dataSource.size
  }

}