package com.nbrk.rates.ui.about

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nbrk.rates.R
import com.nbrk.rates.databinding.FragmentAboutBinding

/**
 * Created by Roman Shakirov on 02-Jul-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class AboutFragment : Fragment() {

  private var _binding: FragmentAboutBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    _binding = FragmentAboutBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val packageInfo: PackageInfo = requireActivity().packageManager.getPackageInfo(requireActivity().packageName, 0)
    binding.tvAbout.text = Html.fromHtml(getString(R.string.about_text).replace("VERSION_NUMBER",
      packageInfo.versionCode.toString()))
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }
}