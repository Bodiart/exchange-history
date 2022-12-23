package com.bodiart.exchange_history.data.entity.api

import com.google.gson.annotations.SerializedName

data class CurrencyRateApi(
    @SerializedName("r030")
    val id: Int?,
    @SerializedName("txt")
    val fullName: String?,
    @SerializedName("rate")
    val rate: Double?,
    @SerializedName("cc")
    val shortName: String?,
    @SerializedName("exchangedate")
    val exchangeDate: String?
)