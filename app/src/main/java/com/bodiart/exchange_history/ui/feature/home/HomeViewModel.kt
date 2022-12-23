package com.bodiart.exchange_history.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bodiart.exchange_history.data.usecase.CurrencyRatesBuildDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val currencyRatesBuildDateUseCase: CurrencyRatesBuildDateUseCase
) : ViewModel() {

    private val mutableViewStateFlow = MutableStateFlow(HomeState())
    val viewStateFlow = mutableViewStateFlow.asStateFlow()

    private val eventChannel = Channel<HomeEvent>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    fun onCalculateClicked() = viewModelScope.launch {
        eventChannel.send(HomeEvent.ShowDatePickerDialog)
    }

    fun onOpenHistoryClicked() = viewModelScope.launch {
        eventChannel.send(HomeEvent.OpenHistory)
    }

    fun onDateChanged(date: LocalDate) = viewModelScope.launch {
        val ratesDate = currencyRatesBuildDateUseCase.invoke(date)
        eventChannel.send(HomeEvent.OpenCalculator(ratesDate))
    }
}