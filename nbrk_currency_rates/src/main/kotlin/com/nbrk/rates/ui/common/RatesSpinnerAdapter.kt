package com.nbrk.rates.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.databinding.SpinnerItemRatesBinding
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
    val binding = SpinnerItemRatesBinding.inflate(LayoutInflater.from(parent.context))
    val imageId = parent.context.getImageId(dataSource[position].currencyCode)
    binding.flag.setImageResource(imageId)
    binding.tvCurrencyCode.text = dataSource[position].currencyCode
    return binding.root
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