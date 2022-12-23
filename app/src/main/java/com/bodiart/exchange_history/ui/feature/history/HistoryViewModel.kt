package com.bodiart.exchange_history.ui.feature.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bodiart.exchange_history.data.entity.data.CurrencyOperation
import com.bodiart.exchange_history.data.usecase.SavedCurrencyOperationsGetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val savedCurrencyOperationsGetUseCase: SavedCurrencyOperationsGetUseCase
) : ViewModel() {

    private val mutableViewStateFlow = MutableStateFlow(
        HistoryState(listOf())
    )
    val viewStateFlow = mutableViewStateFlow.asStateFlow()

    private val eventChannel = Channel<HistoryEvent>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        setupHistory()
    }

    private fun setupHistory() = viewModelScope.launch {
        savedCurrencyOperationsGetUseCase.invoke().fold(
            onSuccess = { operations ->
                mutableViewStateFlow.update {
                    it.copy(operations = mapOperationsToScreen(operations))
                }
            },
            onFailure = {
                // handle error
            }
        )
    }

    private fun mapOperationsToScreen(operations: List<CurrencyOperation>): List<HistoryState.Operation> {
        return operations.map {
            HistoryState.Operation(
                originalCurrency = it.originalCurrency,
                originalCurrencyAmount = it.originalCurrencyAmount.toString(),
                targetCurrency = it.targetCurrency,
                targetCurrencyAmount = it.targetCurrencyAmount.toString()
            )
        }
    }
}