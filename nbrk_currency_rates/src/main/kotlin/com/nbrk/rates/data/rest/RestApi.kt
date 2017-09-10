package com.nbrk.rates.data.rest

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.nbrk.rates.BuildConfig
import com.nbrk.rates.data.rest.entities.CurrencyRates
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by rpagyc on 15-Jan-16.
 */
interface RestApi {

  companion object {

    fun create(): RestApi {

      val API_BASE_URL = "http://www.nationalbank.kz/rss/"

      val httpClient = OkHttpClient.Builder()

      if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClient.addInterceptor(logging)
        httpClient.addNetworkInterceptor(StethoInterceptor())
      }

      val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .client(httpClient.build())
        .build()

      return retrofit.create(RestApi::class.java)
    }
  }

  @GET("get_rates.cfm")
  fun getCurrencyRates(@Query("fdate") date: String): Single<CurrencyRates>
}