package com.nbrk.rates.ui.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.nbrk.rates.R

/**
 * Created by Roman Shakirov on 20-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class SettingsFragment : PreferenceFragmentCompat() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // set default white background to avoid transparency
    view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.background_material_light))
  }

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
  }

}