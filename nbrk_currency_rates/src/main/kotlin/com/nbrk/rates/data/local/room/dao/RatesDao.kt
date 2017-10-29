package com.nbrk.rates.data.local.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.nbrk.rates.data.local.room.model.RoomRatesItem
import io.reactivex.Flowable
import java.util.*

/**
 * Created by Roman Shakirov on 11-Jun-17.
 * DigitTonic Studio
 * support@digittonic.com
 */
@Dao
interface RatesDao {

  @Query("select * from rates where date = :startDate order by currencyCode")
  fun getRates(startDate: Date): Flowable<List<RoomRatesItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveRates(rates: List<RoomRatesItem>)

}