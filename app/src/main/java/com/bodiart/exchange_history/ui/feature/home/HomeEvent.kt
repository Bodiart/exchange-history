package com.bodiart.exchange_history.ui.feature.home

sealed class HomeEvent {
    object ShowDatePickerDialog : HomeEvent()
    object HideDatePickerDialog : HomeEvent()
    object OpenHistory : HomeEvent()
    data class OpenCalculator(val date: String) : HomeEvent()
}