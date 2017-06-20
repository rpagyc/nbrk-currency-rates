package com.nbrk.rates.settings.view.activities

import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.v7.widget.Toolbar
import com.nbrk.rates.R
import com.nbrk.rates.base.ToolbarManager
import com.nbrk.rates.settings.presenter.SettingsPresenter
import nucleus.factory.RequiresPresenter
import nucleus.view.NucleusActivity
import org.jetbrains.anko.find

@RequiresPresenter(SettingsPresenter::class)
class SettingsActivity : NucleusActivity<SettingsPresenter>(),
  ToolbarManager {

  override val toolbar by lazy { find<Toolbar>(R.id.toolbar) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    toolbarTitle = getString(R.string.settings)
    enableHomeAsUp { onBackPressed() }
    // display the fragment as the main content
    fragmentManager.beginTransaction().replace(R.id.content_frame, SettingsFragmentOld()).commit()
  }

  class SettingsFragmentOld : PreferenceFragment() {
    override fun onCreate(paramBundle: Bundle?) {
      super.onCreate(paramBundle)
      addPreferencesFromResource(R.xml.preferences)
    }
  }

}
