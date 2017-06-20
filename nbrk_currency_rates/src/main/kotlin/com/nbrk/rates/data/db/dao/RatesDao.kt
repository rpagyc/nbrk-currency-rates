package com.nbrk.rates.data.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nbrk.rates.entities.RatesItem
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
@Dao
interface RatesDao {

  @Query("select * from dataSource where date = :arg0")
  fun getRates(startDate: Date): Flowable<List<RatesItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveRates(rates: List<RatesItem>): Unit

}