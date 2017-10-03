package com.nbrk.rates.extensions

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable

/**
 * Created by Roman Shakirov on 29-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
// Add the following to your build.gradle file
// implementation "android.arch.lifecycle:reactivestreams:1.0.0-beta1"
/**
 * Converts LiveData into a Flowable
 */
fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner): Flowable<T> =
  Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))


/**
 * Converts a Flowable into LiveData
 */
fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)