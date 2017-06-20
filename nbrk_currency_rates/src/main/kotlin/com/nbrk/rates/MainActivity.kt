package com.nbrk.rates

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.nbrk.rates.base.BaseLifecycleActivity
import com.nbrk.rates.converter.ConverterFragment
import com.nbrk.rates.extensions.TAG
import com.nbrk.rates.rates.RatesViewModel
import com.nbrk.rates.settings.SettingsFragment
import java.util.*

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class MainActivity : BaseLifecycleActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

  private val viewModel: RatesViewModel by lazy { ViewModelProviders.of(this).get(RatesViewModel::class.java) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer.addDrawerListener(toggle)
    toggle.syncState()

    val navigationView = findViewById(R.id.nav_view) as NavigationView
    navigationView.setNavigationItemSelectedListener(listener)

    if (savedInstanceState == null) {
      title = getString(R.string.rates)
      val ratesFragment = RatesFragment()
      supportFragmentManager
        .beginTransaction()
        .add(R.id.content_main, ratesFragment, ratesFragment.TAG())
        .commit()
    }
  }

  override fun onBackPressed() {
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.action_settings -> return true
      else -> return super.onOptionsItemSelected(item)
    }
  }

  val listener: (MenuItem) -> Boolean = {
    when (it.itemId) {
      R.id.nav_rates -> {
        title = getString(R.string.rates)
        val ratesFragment = RatesFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, ratesFragment, ratesFragment.TAG())
          .commit()
      }
      R.id.nav_history -> {
        title = getString(R.string.history)
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
          calendar.set(y, m, d)
          viewModel.setDate(calendar.time)
        },
          calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH),
          calendar.get(Calendar.DAY_OF_MONTH)).show()
      }
      R.id.nav_chart -> {
        title = getString(R.string.chart)
      }
      R.id.nav_convert -> {
        title = getString(R.string.convert)
        val converterFragment = ConverterFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, converterFragment, converterFragment.TAG())
          .commit()
      }
      R.id.nav_settings -> {
        title = getString(R.string.settings)
        val settingsFragment = SettingsFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, settingsFragment, settingsFragment.TAG())
          .commit()
      }
      R.id.nav_share -> {
      }
      R.id.nav_send -> {
      }
    }
    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)

    true
  }

  override fun onPreferenceStartScreen(preferenceFragmentCompat: PreferenceFragmentCompat, preferenceScreen: PreferenceScreen): Boolean {
    val settingsFragment = SettingsFragment()
    val args = Bundle()
    args.putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, preferenceScreen.key)
    settingsFragment.arguments = args
    supportFragmentManager
      .beginTransaction()
      .add(R.id.content_main, settingsFragment, preferenceScreen.key)
      .addToBackStack(preferenceScreen.key)
      .commit()
    return true
  }
}