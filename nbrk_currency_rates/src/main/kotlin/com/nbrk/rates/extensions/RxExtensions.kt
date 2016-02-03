package com.nbrk.rates.extensions

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by rpagyc on 09-Dec-15.
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());