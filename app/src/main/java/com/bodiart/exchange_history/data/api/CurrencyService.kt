package com.bodiart.exchange_history.data.api

import com.bodiart.exchange_history.data.entity.api.CurrencyRateApi
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyService {
    @GET("NBUStatService/v1/statdirectory/exchange?json")
    suspend fun getCurrenciesRate(@Query("date") date: String): List<CurrencyRateApi>
}