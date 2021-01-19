package com.nbrk.rates.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * Created by Roman Shakirov on 29-Sep-17.
 * DigitTonic Studio
 * support@digittonic.com
 */

/**
 * Converts LiveData into a Flowable
 */
fun <T> LiveData<T>.toFlowable(lifecycleOwner: LifecycleOwner): Flowable<T> =
  Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))


/**
 * Converts a Flowable into LiveData
 */
fun <T> Flowable<T>.toLiveData(): LiveData<T> = LiveDataReactiveStreams.fromPublisher(this)

fun <T> applySchedulers(): FlowableTransformer<T, T> {
  return FlowableTransformer { flowable ->
    flowable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) }
}