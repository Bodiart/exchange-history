package com.bodiart.exchange_history.ui.feature.history

data class HistoryState(
    val operations: List<Operation>
) {
    data class Operation(
        val originalCurrency: String,
        val originalCurrencyAmount: String,
        val targetCurrency: String,
        val targetCurrencyAmount: String
    )
}