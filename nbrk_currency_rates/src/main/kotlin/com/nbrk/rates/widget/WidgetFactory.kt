package com.nbrk.rates.widget

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.nbrk.rates.App
import com.nbrk.rates.R
import com.nbrk.rates.data.RatesLocalDataSource
import com.nbrk.rates.data.RatesRemoteDataSource
import com.nbrk.rates.entities.RatesItem
import com.nbrk.rates.extensions.getDrawable
import java.util.*

/**
 * Created by rpagyc on 30.10.2015.
 */
class WidgetFactory(val context: Context, intent: Intent) :
  RemoteViewsService.RemoteViewsFactory {

  var rates = ArrayList<RatesItem>()
  val sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
  val remoteDataSource = RatesRemoteDataSource()
  val localDataSource = RatesLocalDataSource()

  fun setRates() {
    val date = Calendar.getInstance().time
    localDataSource
      .getRates(date)
      .onErrorResumeNext {
        remoteDataSource.getRates(date)
          .doOnSuccess { localDataSource.saveRates(it) }
      }
      .subscribe(
        { it ->
          rates = it.rates.filter {
                        sharedPref.getBoolean("widget_show_${it.currencyCode}", true)
          } as ArrayList<RatesItem>
        },
        { error -> error(error) }
      )
  }

  override fun onCreate() {
  }

  override fun onDataSetChanged() {
    setRates()
  }

  override fun onDestroy() {
  }

  override fun getCount(): Int {
    return rates.size
  }

  override fun getViewAt(position: Int): RemoteViews {
    val remoteViews = RemoteViews(context.packageName, R.layout.widget_row)
    if (position < rates.size) {
      val ratesItem = rates[position]
      remoteViews.setTextViewText(R.id.tvCurrencyCode, ratesItem.currencyCode)
      remoteViews.setTextViewText(R.id.tvCurrencyName, "${ratesItem.quantity} ${ratesItem.currencyName?.toLowerCase()}")
      remoteViews.setTextViewText(R.id.tvPrice, ratesItem.price.toString())
      remoteViews.setTextViewText(R.id.tvChange, ratesItem.change.toString())
      remoteViews.setImageViewResource(R.id.flag, ratesItem.currencyCode?.getDrawable())
      remoteViews.setViewVisibility(R.id.imgChange, View.VISIBLE)
      if (ratesItem.index.equals("UP")) {
        remoteViews.setImageViewResource(R.id.imgChange, R.mipmap.ic_up)
        remoteViews.setTextColor(R.id.tvChange, Color.rgb(90, 150, 55));
        remoteViews.setTextViewText(R.id.tvChange, "+${ratesItem.change.toString()}")
      } else if (ratesItem.index.equals("DOWN")) {
        remoteViews.setTextColor(R.id.tvChange, Color.RED)
        remoteViews.setImageViewResource(R.id.imgChange, R.mipmap.ic_down)
      } else {
        remoteViews.setTextColor(R.id.tvChange, Color.LTGRAY)
        remoteViews.setViewVisibility(R.id.imgChange, View.GONE)
      }
    }
    return remoteViews
  }                                                                                   

  override fun getLoadingView(): RemoteViews? {
    return null
  }

  override fun getViewTypeCount(): Int {
    return 1
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun hasStableIds(): Boolean {
    return true
  }
}
