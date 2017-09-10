package com.nbrk.rates

import android.Manifest
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nbrk.rates.about.AboutFragment
import com.nbrk.rates.base.BaseLifecycleActivity
import com.nbrk.rates.chart.ChartFragment
import com.nbrk.rates.converter.ConverterFragment
import com.nbrk.rates.extensions.TAG
import com.nbrk.rates.rates.RatesFragment
import com.nbrk.rates.rates.RatesViewModel
import com.nbrk.rates.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class MainActivity : BaseLifecycleActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

  private val ratesViewModel: RatesViewModel by lazy { ViewModelProviders.of(this).get(RatesViewModel::class.java) }
  companion object {
    const val REQUEST_STORAGE = 1
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

//    val toolbar = findViewById(R.id.toolbar) as Toolbar
    setSupportActionBar(toolbar)

//    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

//    val navigationView = findViewById(R.id.nav_view) as NavigationView
    nav_view.setNavigationItemSelectedListener(listener)

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
//    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
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
      R.id.action_about -> {
        val aboutFragment = AboutFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, aboutFragment, aboutFragment.TAG())
          .commit()
      }
      else -> return super.onOptionsItemSelected(item)
    }
    return true
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
          ratesViewModel.setDate(calendar.time)
        },
          calendar.get(Calendar.YEAR),
          calendar.get(Calendar.MONTH),
          calendar.get(Calendar.DAY_OF_MONTH)).show()
      }
      R.id.nav_chart -> {
        title = getString(R.string.chart)
        val chartFragment = ChartFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, chartFragment, chartFragment.TAG())
          .commit()
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
          && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
          shareScreenshot()
        }
        else {
          ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE)
        }
      }
      R.id.nav_rating -> {
        try {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
        }
      }
      R.id.nav_about -> {
        val aboutFragment = AboutFragment()
        supportFragmentManager
          .beginTransaction()
          .replace(R.id.content_main, aboutFragment, aboutFragment.TAG())
          .commit()
      }
    }

//    val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
    drawer_layout.closeDrawer(GravityCompat.START)

    true
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    when (requestCode) {
      REQUEST_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) shareScreenshot()
    }
  }

  fun shareScreenshot() {
    val bm = screenShot(findViewById(R.id.content_main))
    val file = saveBitmap(bm, "screenshot.png")
    val uri = Uri.fromFile(File(file.absolutePath))
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND
    shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name))
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
    shareIntent.type = "image/*"
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
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

  private fun screenShot(view: View): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
  }

  private fun saveBitmap(bm: Bitmap, fileName: String): File {
    val path = Environment.getExternalStorageDirectory().absolutePath + "/Screenshots"
    val dir = File(path)
    if (!dir.exists())
      dir.mkdirs()
    val file = File(dir, fileName)
    try {
      val fOut = FileOutputStream(file)
      bm.compress(Bitmap.CompressFormat.PNG, 90, fOut)
      fOut.flush()
      fOut.close()
    } catch (e: Exception) {
      e.printStackTrace()
    }

    return file
  }

}