package com.nbrk.rates.base

import android.support.v7.graphics.drawable.DrawerArrowDrawable
import android.support.v7.widget.Toolbar
import com.nbrk.rates.App
import com.nbrk.rates.R
import com.nbrk.rates.extensions.ctx
import com.nbrk.rates.settings.view.activities.SettingsActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Created by rpagyc on 15-Dec-15.
 */
interface ToolbarManager {

  val toolbar: Toolbar

  var toolbarTitle: String
    get() = toolbar.title.toString()
    set(value) {
      toolbar.title = value
    }

  fun initToolbar() {
    toolbar.inflateMenu(R.menu.main_menu)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.menu_refresh -> null
        R.id.menu_date -> null
        R.id.menu_converter -> null
        R.id.menu_chart -> null
        R.id.menu_settings -> toolbar.ctx.startActivity<SettingsActivity>()
        else -> App.instance.toast("Unknown option")
      }
      true
    }
  }

  fun enableHomeAsUp(up: () -> Unit) {
    toolbar.navigationIcon = createUpDrawable()
    toolbar.setNavigationOnClickListener { up() }
  }

  private fun createUpDrawable() = DrawerArrowDrawable(toolbar.context).apply {
    progress = 1f
  }

}