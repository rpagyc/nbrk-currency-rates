package com.nbrk.rates.settings

import android.os.Bundle
import com.nbrk.rates.R
import com.nbrk.rates.base.BasePreferenceFragment

/**
 * Created by Roman Shakirov on 20-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class SettingsFragment : BasePreferenceFragment() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
  }

}