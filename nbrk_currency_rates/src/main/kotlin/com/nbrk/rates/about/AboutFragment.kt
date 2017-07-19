package com.nbrk.rates.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nbrk.rates.BuildConfig
import com.nbrk.rates.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Created by Roman Shakirov on 02-Jul-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class AboutFragment : Fragment() {
  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_about, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tvAbout.text = Html.fromHtml(getString(R.string.about_text).replace("VERSION_NUMBER", BuildConfig.VERSION_NAME))
  }
}