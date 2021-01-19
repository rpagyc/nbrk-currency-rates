package com.nbrk.rates.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.util.getImageId

/**
* Created by Roman Shakirov on 25-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
class RatesSpinnerAdapter : BaseAdapter() {

  var dataSource : List<RatesItem> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.spinner_item_rates,
      parent, false)
    val imageId = parent.context.getImageId(dataSource[position].currencyCode)
    //itemView.flag.setImageResource(imageId)
    //itemView.tvCurrencyCode.text = dataSource[position].currencyCode
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