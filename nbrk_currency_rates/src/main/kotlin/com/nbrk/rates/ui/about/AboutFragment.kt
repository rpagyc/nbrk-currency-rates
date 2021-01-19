package com.nbrk.rates.ui.about

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.multidex.BuildConfig
import com.nbrk.rates.R
import com.nbrk.rates.databinding.FragmentAboutBinding

/**
 * Created by Roman Shakirov on 02-Jul-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class AboutFragment : Fragment() {

  private var fragmentAboutBinding: FragmentAboutBinding? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = FragmentAboutBinding.bind(view)
    fragmentAboutBinding = binding
    binding.tvAbout.text = Html.fromHtml(getString(R.string.about_text).replace("VERSION_NUMBER",
      BuildConfig.VERSION_NAME))
  }

  override fun onDestroy() {
    super.onDestroy()
    fragmentAboutBinding = null
  }
}