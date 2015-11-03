package com.nbrk.rates;

import com.nbrk.rates.model.CurrencyRates;
import retrofit.http.GET;
import retrofit.http.Query;

public interface NbrkApi {

    String API_URL = "http://www.nationalbank.kz/rss";

    @GET("/get_rates.cfm")
    CurrencyRates getCurrencyRates(@Query("fdate") String date);
}
