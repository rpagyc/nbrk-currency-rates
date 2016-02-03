package com.nbrk.rates.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.preference.PreferenceManager
import android.widget.RemoteViews
import com.nbrk.rates.App
import com.nbrk.rates.R
import com.nbrk.rates.widget.view.activities.ConfigActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rpagyc on 29.10.2015.
 */
class WidgetProvider : AppWidgetProvider() {

  var date = Calendar.getInstance()
  var time = SimpleDateFormat("HH:mm")

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    for (appWidgetId in appWidgetIds) {
      val svcIntent = Intent(context, WidgetService::class.java)
      val widget = RemoteViews(context.packageName, R.layout.widget_layout)

      val sharedPref = PreferenceManager.getDefaultSharedPreferences(App.instance)
      val color = sharedPref.getString("widgetBackgroundColor","#ffffff")
      val opacity = sharedPref.getInt("sbOpacity", 100) * 255 / 100 * 0x01000000
      val widgetBackground = Color.parseColor(color) + opacity
      widget.setInt(R.id.widgetRates, "setBackgroundColor", widgetBackground)

      svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)))

      widget.setRemoteAdapter(R.id.lv_widget_rates, svcIntent)
      widget.setTextViewText(R.id.widget_time, time.format(date.time))

      val config = Intent(context, ConfigActivity::class.java)
      widget.setOnClickPendingIntent(R.id.widget_config, PendingIntent.getActivity(context, 0, config, 0))

      val update = Intent(context, WidgetProvider::class.java)
      update.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
      update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

      widget.setOnClickPendingIntent(R.id.widget_update,
        PendingIntent.getBroadcast(context, 0, update, PendingIntent.FLAG_UPDATE_CURRENT))

      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_rates)

      appWidgetManager.updateAppWidget(appWidgetId, widget)
    }
  }

}
