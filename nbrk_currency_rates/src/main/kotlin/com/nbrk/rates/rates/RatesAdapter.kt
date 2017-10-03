package com.nbrk.rates.rates

import android.graphics.Color
import android.view.View
import com.nbrk.rates.R
import com.nbrk.rates.base.BaseAdapter
import com.nbrk.rates.base.BaseViewHolder
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.getDrawable
import kotlinx.android.synthetic.main.list_item_rates.view.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class RatesAdapter : BaseAdapter<RatesItem, RatesAdapter.RatesViewHolder>() {

  override fun getItemViewId(): Int = R.layout.list_item_rates

  override fun instantiateViewHolder(view: View?): RatesViewHolder = RatesViewHolder(view)

  class RatesViewHolder(itemView: View?) : BaseViewHolder<RatesItem>(itemView) {
    override fun onBind(item: RatesItem) {
      itemView.flag.setImageResource(item.currencyCode.getDrawable())
      itemView.tvCurrencyCode.text = item.currencyCode
      itemView.tvCurrencyName.text = "${item.quantity} ${item.currencyName.toLowerCase()}"
      itemView.tvPrice.text = "${item.price}"
      itemView.tvChange.text = "${item.change}"
      itemView.imgChange.visibility = View.VISIBLE
      itemView.tvChange.visibility = View.VISIBLE

      when {
        item.index == "UP" -> {
          itemView.tvChange.setTextColor(Color.rgb(90, 150, 55))
          itemView.tvChange.text = "+${item.change}"
          itemView.imgChange.setImageResource(R.mipmap.ic_up)
        }
        item.index == "DOWN" -> {
          itemView.tvChange.setTextColor(Color.RED)
          itemView.imgChange.setImageResource(R.mipmap.ic_down)
        }
        else -> {
          itemView.tvChange.setTextColor(Color.LTGRAY)
          itemView.imgChange.visibility = View.GONE
          itemView.tvChange.visibility = View.GONE
        }
      }
    }
  }
}