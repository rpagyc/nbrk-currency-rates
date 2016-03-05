package com.nbrk.rates.widget.view.activities

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.widget.Toolbar
import com.nbrk.rates.R
import com.nbrk.rates.base.ToolbarManager
import com.nbrk.rates.widget.WidgetProvider
import com.nbrk.rates.widget.presenter.ConfigPresenter
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusActivity
import org.jetbrains.anko.find

/**
 * Created by rpagyc on 09.11.2015.
 */
@RequiresPresenter(ConfigPresenter::class)
class ConfigActivity : NucleusActivity<ConfigPresenter>(),
  ToolbarManager {

  override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    toolbarTitle = getString(R.string.widget_config)
    enableHomeAsUp { onBackPressed() }
    // display the fragment as the main content
    fragmentManager.beginTransaction().replace(R.id.content_frame, SettingsFragment()).commit()
  }

  class SettingsFragment : PreferenceFragment() {
    override fun onCreate(paramBundle: Bundle?) {
      super.onCreate(paramBundle)
      addPreferencesFromResource(R.xml.widget_config)
    }
  }

  override fun onPause() {
    super.onPause()
    val widgetManager = AppWidgetManager.getInstance(this)
    val widgetComponent = ComponentName(this, WidgetProvider::class.java)
    val widgetIds = widgetManager.getAppWidgetIds(widgetComponent)
    val update = Intent(this,WidgetProvider::class.java)
    update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
    update.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
    sendBroadcast(update)
    finish()
  }

}
