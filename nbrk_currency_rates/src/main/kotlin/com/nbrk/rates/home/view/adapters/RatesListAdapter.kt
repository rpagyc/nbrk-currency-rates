package com.nbrk.rates.home.view.adapters

import android.graphics.Color
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.nbrk.rates.App
import com.nbrk.rates.R
import com.nbrk.rates.extensions.getDrawable
import com.nbrk.rates.home.model.entities.Rates
import com.nbrk.rates.home.model.entities.RatesItem
import kotlinx.android.synthetic.main.list_item_rates.view.*
import org.jetbrains.anko.layoutInflater

/**
 * Created by rpagyc on 18-Jan-16.
 */
class RatesListAdapter : RecyclerView.Adapter<RatesListAdapter.ViewHolder>() {

  var rates = emptyList<RatesItem>()

  fun setData(rates: Rates) {
    val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.instance)
    this.rates = rates.rates.filter {
      sharedPref.getBoolean("pref_key_show_${it.currencyCode}", true)
    }
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(rates[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = parent.context.layoutInflater.inflate(R.layout.list_item_rates, parent, false)
    return ViewHolder(view)
  }

  override fun getItemCount(): Int {
    return rates.size()
  }

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(ratesItem: RatesItem) {
      itemView.flag.setImageResource(ratesItem.currencyCode.getDrawable())
      itemView.tvCurrencyCode.text = ratesItem.currencyCode
      itemView.tvCurrencyName.text = "${ratesItem.quantity} ${ratesItem.currencyName.toLowerCase()}"
      itemView.tvPrice.text = ratesItem.price.toString()
      itemView.tvChange.text = ratesItem.change.toString()

      if (ratesItem.index.equals("UP")) {
        itemView.tvChange.setTextColor(Color.rgb(90, 150, 55));
        itemView.tvChange.text = "+${itemView.tvChange.text}"
      } else if (ratesItem.index.equals("DOWN")) {
        itemView.tvChange.setTextColor(Color.RED);
      } else {
        itemView.tvChange.setTextColor(Color.LTGRAY);
      }
    }
  }
}