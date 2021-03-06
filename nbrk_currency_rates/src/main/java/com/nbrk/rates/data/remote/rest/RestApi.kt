package com.nbrk.rates.data.remote.rest

import com.nbrk.rates.BuildConfig
import com.nbrk.rates.data.remote.rest.model.RestRates
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
* Created by Roman Shakirov on 15-Jan-16.
* DigitTonic Studio
* support@digittonic.com
*/
interface RestApi {

  companion object {

    fun getInstance(): RestApi {

      val API_BASE_URL = "https://www.nationalbank.kz/rss/"

      val httpClient = OkHttpClient.Builder()

      if (BuildConfig.DEBUG) {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        httpClient.addInterceptor(logging)
      }

      val retrofit = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(SimpleXmlConverterFactory
          .createNonStrict(Persister(AnnotationStrategy())))
        .client(httpClient.build())
        .build()

      return retrofit.create(RestApi::class.java)
    }
  }

  @GET("get_rates.cfm")
  fun getRates(@Query("fdate") date: String): Single<RestRates>
}