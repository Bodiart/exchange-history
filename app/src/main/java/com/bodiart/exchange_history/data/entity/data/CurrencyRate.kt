package com.bodiart.exchange_history.data.entity.data

data class CurrencyRate(
    val id: Int,
    val fullName: String,
    val shortName: String,
    val rate: Double
)