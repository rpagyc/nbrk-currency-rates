package com.nbrk.rates.base

import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
abstract class BaseLifecycleActivity<T: AndroidViewModel> : AppCompatActivity(), LifecycleRegistryOwner {

  abstract val viewModelClass: Class<T>

  protected lateinit var viewModel: T

  private val registry = LifecycleRegistry(this)

  override fun getLifecycle(): LifecycleRegistry = registry

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this).get(viewModelClass)
  }

}