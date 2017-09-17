package com.nbrk.rates.data.db

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.content.Context
import com.nbrk.rates.data.db.AppDatabase.Companion.DATABASE_NAME
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
object DatabaseCreator {

  private val isDatabaseCreated = MutableLiveData<Boolean>()

  lateinit var database: AppDatabase

  private val mInitializing = AtomicBoolean(true)

  fun createDb(context: Context) {
    if (!mInitializing.compareAndSet(true, false)) {
      return
    }

    isDatabaseCreated.value = false

    Completable.fromAction {
      database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()
    }
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ isDatabaseCreated.value = true }, { it.printStackTrace() })
  }
}