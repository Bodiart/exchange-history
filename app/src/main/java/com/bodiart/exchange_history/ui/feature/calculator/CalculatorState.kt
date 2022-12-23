package com.bodiart.exchange_history.ui.feature.calculator

data class CalculatorState(
    val isLoadingVisible: Boolean,
    val originalCurrency: String?,
    val originalCurrencyAmount: String,
    val targetCurrency: String?,
    val targetCurrencyAmount: String
)