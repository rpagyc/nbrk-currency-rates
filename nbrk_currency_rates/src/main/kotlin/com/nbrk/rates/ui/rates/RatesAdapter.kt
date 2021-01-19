package com.nbrk.rates.ui.rates

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nbrk.rates.R
import com.nbrk.rates.data.local.domain.model.RatesItem
import com.nbrk.rates.databinding.ListItemRatesBinding
import com.nbrk.rates.util.getImageId

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

  var dataSource : List<RatesItem> = emptyList()
    set(value) {
      field = value
      notifyDataSetChanged()
    }

  class RatesViewHolder(private val itemRatesBinding: ListItemRatesBinding) : RecyclerView.ViewHolder(itemRatesBinding.root)
  {
    fun bind(item: RatesItem) {
      val imageId = itemView.context.getImageId(item.currencyCode)
      itemRatesBinding.flag.setImageResource(imageId)
      itemRatesBinding.tvCurrencyCode.text = item.currencyCode
      itemRatesBinding.tvCurrencyName.text = "${item.quantity} ${item.currencyName.toLowerCase()}"
      itemRatesBinding.tvPrice.text = "${item.price}"
      itemRatesBinding.tvChange.text = "${item.change}"
      itemRatesBinding.imgChange.visibility = View.VISIBLE
      itemRatesBinding.tvChange.visibility = View.VISIBLE

      when {
        item.index == "UP" -> {
          itemRatesBinding.tvChange.setTextColor(Color.rgb(90, 150, 55))
          itemRatesBinding.tvChange.text = "+${item.change}"
          itemRatesBinding.imgChange.setImageResource(R.mipmap.ic_up)
        }
        item.index == "DOWN" -> {
          itemRatesBinding.tvChange.setTextColor(Color.RED)
          itemRatesBinding.imgChange.setImageResource(R.mipmap.ic_down)
        }
        else -> {
          itemRatesBinding.tvChange.setTextColor(Color.LTGRAY)
          itemRatesBinding.imgChange.visibility = View.GONE
          itemRatesBinding.tvChange.visibility = View.GONE
        }
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
    val itemRatesBinding = ListItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RatesViewHolder(itemRatesBinding)
  }

  override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
    val item = dataSource[position]
    holder.bind(item)
  }

  override fun getItemCount(): Int = dataSource.size

}