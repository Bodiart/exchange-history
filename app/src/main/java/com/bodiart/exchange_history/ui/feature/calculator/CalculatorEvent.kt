package com.bodiart.exchange_history.ui.feature.calculator

sealed class CalculatorEvent {
    object Back : CalculatorEvent()
    data class ShowOriginalCurrencyPickerDialog(val currencies: List<String>) : CalculatorEvent()
    data class ShowTargetCurrencyPickerDialog(val currencies: List<String>) : CalculatorEvent()
    object HideOriginalCurrencyPickerDialog : CalculatorEvent()
    object HideTargetCurrencyPickerDialog : CalculatorEvent()
}