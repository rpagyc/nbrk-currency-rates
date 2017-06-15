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
@Suppress("LeakingThis")
abstract class BaseLifecycleActivity : AppCompatActivity(), LifecycleRegistryOwner {

  private val registry = LifecycleRegistry(this)

  override fun getLifecycle(): LifecycleRegistry = registry

}