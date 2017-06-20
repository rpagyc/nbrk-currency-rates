package com.nbrk.rates.base

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.View
import com.nbrk.rates.R

/**
 * Created by Roman Shakirov on 20-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
abstract class BasePreferenceFragment : PreferenceFragmentCompat() {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    // set default white background to avoid transparency
    view.setBackgroundColor(ContextCompat.getColor(context, R.color.background_material_light))
  }
}