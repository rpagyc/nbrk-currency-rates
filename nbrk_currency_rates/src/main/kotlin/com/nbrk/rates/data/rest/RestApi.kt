package com.nbrk.rates.data.rest

import com.facebook.stetho.okhttp.StethoInterceptor
import com.nbrk.rates.BuildConfig
import com.nbrk.rates.data.rest.entities.CurrencyRates
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.Response
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import retrofit.SimpleXmlConverterFactory
import retrofit.http.GET
import retrofit.http.Query
import rx.Observable

/**
 * Created by rpagyc on 15-Jan-16.
 */
interface RestApi {

  companion object {

    fun create(): RestApi {

      val API_BASE_URL = "http://www.nationalbank.kz/rss/"

      val httpClient = OkHttpClient()
      if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        httpClient.interceptors().add(logging)
        httpClient.networkInterceptors().add(StethoInterceptor());
      }

      val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .client(httpClient)
        .build()

      return retrofit.create(RestApi::class.java)
    }
  }

  @GET("get_rates.cfm")
  fun getCurrencyRates(@Query("fdate") date: String): Observable<Response<CurrencyRates>>
}