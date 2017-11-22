package com.nbrk.rates.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.alpha
import android.net.Uri
import android.widget.RemoteViews
import com.nbrk.rates.R
import org.jetbrains.anko.defaultSharedPreferences
import java.text.SimpleDateFormat
import java.util.*

/**
* Created by Roman Shakirov on 29.10.2015.
* DigitTonic Studio
* support@digittonic.com
*/
class   WidgetProvider : AppWidgetProvider() {

  var date: Calendar = Calendar.getInstance()
  var time = SimpleDateFormat("HH:mm")

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager,
                        appWidgetIds: IntArray) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    for (appWidgetId in appWidgetIds) {
      val widget = RemoteViews(context.packageName, R.layout.widget_layout)
      configureBackground(widget, context)
      configureRefresh(context, widget, appWidgetIds)
      configureTitle(context, widget)
      configureList(context, widget, appWidgetId)
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_rates)
      appWidgetManager.updateAppWidget(appWidgetId, widget)
    }
  }

  private fun configureBackground(rv: RemoteViews, context: Context) {
    val sharedPref = context.defaultSharedPreferences
    val color = sharedPref.getString("widgetBackgroundColor", "#ffffff")
    val opacity = sharedPref.getInt("sbOpacity", 100) * 255 / 100 * 0x01000000
    val widgetBackground = Color.parseColor(color) + opacity
    rv.setInt(R.id.background_image, "setColorFilter", widgetBackground)
    rv.setInt(R.id.background_image, "setAlpha", alpha(widgetBackground))
  }

  private fun configureRefresh(context: Context, rv: RemoteViews, widgetIds: IntArray) {
    val intent = Intent(context, WidgetProvider::class.java)
    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
    val pIntent = PendingIntent.getBroadcast(context, 0, intent,
      PendingIntent.FLAG_UPDATE_CURRENT)
    rv.setOnClickPendingIntent(R.id.widget_update, pIntent)
  }

  private fun configureTitle(context: Context, rv: RemoteViews) {
    val widgetTitle = context.resources.getString(R.string.last_updated)
    rv.setTextViewText(R.id.widget_title, "$widgetTitle ${time.format(date.time)}")
  }

  private fun configureList(context: Context, rv: RemoteViews, widgetId: Int) {
    val intent = Intent(context, WidgetService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
    intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
    rv.setRemoteAdapter(R.id.lv_widget_rates, intent)
  }

}
