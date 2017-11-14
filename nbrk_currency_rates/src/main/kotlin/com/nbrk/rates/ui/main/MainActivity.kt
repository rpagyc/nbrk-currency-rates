package com.nbrk.rates.ui.main

import android.Manifest
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceScreen
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.nbrk.rates.Injection
import com.nbrk.rates.R
import com.nbrk.rates.ui.about.AboutFragment
import com.nbrk.rates.ui.chart.ChartFragment
import com.nbrk.rates.ui.common.RatesViewModel
import com.nbrk.rates.ui.converter.ConverterFragment
import com.nbrk.rates.ui.rates.RatesFragment
import com.nbrk.rates.ui.settings.SettingsFragment
import com.nbrk.rates.util.TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileOutputStream


/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class MainActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartScreenCallback {

  private val viewModelFactory by lazy { Injection.provideViewModelFactory(this) }
  private val ratesViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(RatesViewModel::class.java)
  }

  companion object {
    const val REQUEST_STORAGE = 1
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
      R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(navigationItemSelectedListener)

    if (savedInstanceState == null) {
      ratesViewModel.refresh()
      showScreen(getString(R.string.rates), RatesFragment())
    }
  }

  override fun onBackPressed() {
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
      R.id.action_about -> showScreen(getString(R.string.about), AboutFragment())
      else -> return super.onOptionsItemSelected(item)
    }
    return true
  }

  private fun showScreen(title: String, fragment: Fragment) {
    this.title = title
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.content_main, fragment, fragment.TAG())
      .commit()
  }

  private val navigationItemSelectedListener: (MenuItem) -> Boolean = {
    when (it.itemId) {
      R.id.nav_rates -> {
        ratesViewModel.refresh()
        showScreen(getString(R.string.rates), RatesFragment())
      }
      R.id.nav_history -> showCalendar()
      R.id.nav_chart -> showScreen(getString(R.string.chart), ChartFragment())
      R.id.nav_convert -> showScreen(getString(R.string.convert), ConverterFragment())
      R.id.nav_settings -> showScreen(getString(R.string.settings), SettingsFragment())
      R.id.nav_share -> shareScreenshot()
      R.id.nav_rating -> rateAppOnMarket()
      R.id.nav_about -> showScreen(getString(R.string.about), AboutFragment())
    }
    drawer_layout.closeDrawer(GravityCompat.START)
    true
  }

  private fun showCalendar() {
    val today = LocalDate.now()

    DatePickerDialog(this,
      { _, y, m, d ->
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dateTitle = LocalDate.of(y, m + 1, d).format(formatter)
        val title = "${getString(R.string.history)} $dateTitle"
        ratesViewModel.date.value = LocalDate.of(y, m + 1, d)
        showScreen(title, RatesFragment())
      },
      today.year,
      today.monthValue - 1,
      today.dayOfMonth).show()
  }

  private fun rateAppOnMarket() {
    try {
      startActivity(Intent(Intent.ACTION_VIEW,
        Uri.parse("market://details?id=$packageName")))
    } catch (ex: ActivityNotFoundException) {
      startActivity(Intent(Intent.ACTION_VIEW,
        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")))
    }
  }

  private fun isPermissionsGranted(): Boolean {
    return ActivityCompat.checkSelfPermission(this,
      Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
      &&
      ActivityCompat.checkSelfPermission(this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
  }

  private fun requestPermissions() {
    ActivityCompat.requestPermissions(this,
      arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
      REQUEST_STORAGE)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                          grantResults: IntArray) {
    when (requestCode) {
      REQUEST_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) shareScreenshot()
    }
  }

  private fun shareScreenshot() {
    if (isPermissionsGranted()) {
      val bm = captureScreen(findViewById(R.id.content_main))
      val file = saveBitmap(bm, "screenshot.png")
      val uri = Uri.fromFile(File(file.absolutePath))
      val shareIntent = Intent()
      shareIntent.action = Intent.ACTION_SEND
      shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name))
      shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
      shareIntent.type = "image/*"
      shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    } else {
      requestPermissions()
    }
  }

  override fun onPreferenceStartScreen(preferenceFragmentCompat: PreferenceFragmentCompat,
                                       preferenceScreen: PreferenceScreen): Boolean {
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

  private fun captureScreen(view: View): Bitmap {
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