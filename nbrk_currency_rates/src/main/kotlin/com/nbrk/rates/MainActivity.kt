package com.nbrk.rates

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle

/**
 * Created by Roman Shakirov on 14-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
class MainActivity : LifecycleActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      val ratesFragment = RatesFragment()
      supportFragmentManager.beginTransaction().add(R.id.fragment_container, ratesFragment, RatesFragment.TAG).commit()
    }
  }

}