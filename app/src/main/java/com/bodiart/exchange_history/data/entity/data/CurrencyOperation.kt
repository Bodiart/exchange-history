package com.bodiart.exchange_history.data.entity.data

class CurrencyOperation(
    val date: String,
    val originalCurrency: String,
    val originalCurrencyAmount: Int,
    val targetCurrency: String,
    val targetCurrencyAmount: Double
)