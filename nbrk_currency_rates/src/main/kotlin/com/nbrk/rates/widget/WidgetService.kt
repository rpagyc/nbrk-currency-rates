package com.nbrk.rates.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by rpagyc on 30.10.2015.
 */
class WidgetService : RemoteViewsService() {
  override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
    return WidgetFactory(this.applicationContext, intent)
  }
}
