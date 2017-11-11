package com.nbrk.rates.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
* Created by Roman Shakirov on 30.10.2015.
* DigitTonic Studio
* support@digittonic.com
*/
class WidgetService : RemoteViewsService() {
  override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory {
    return WidgetFactory(this.applicationContext)
  }
}
