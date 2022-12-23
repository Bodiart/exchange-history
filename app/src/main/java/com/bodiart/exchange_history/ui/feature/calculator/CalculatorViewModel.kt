package com.bodiart.exchange_history.ui.feature.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bodiart.exchange_history.data.entity.data.CurrencyOperation
import com.bodiart.exchange_history.data.entity.data.CurrencyRate
import com.bodiart.exchange_history.data.usecase.CalculateRateUseCase
import com.bodiart.exchange_history.data.usecase.CurrencyOperationSaveUseCase
import com.bodiart.exchange_history.data.usecase.CurrencyRatesGetUseCase
import com.bodiart.exchange_history.ui.feature.main.NavigationNode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val currencyRatesGetUseCase: CurrencyRatesGetUseCase,
    private val calculateRateUseCase: CalculateRateUseCase,
    private val currencyOperationSaveUseCase: CurrencyOperationSaveUseCase
) : ViewModel() {

    private val argDate = requireNotNull(
        savedStateHandle.get<String>(NavigationNode.Calculator.argDate)
    )

    private val mutableViewStateFlow = MutableStateFlow(
        CalculatorState(
            isLoadingVisible = true,
            originalCurrency = null,
            originalCurrencyAmount = "",
            targetCurrency = null,
            targetCurrencyAmount = ""
        )
    )
    val viewStateFlow = mutableViewStateFlow.asStateFlow()

    private val eventChannel = Channel<CalculatorEvent>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    private var currencyRates = listOf<CurrencyRate>()
    private val selectedOriginalCurrencyFlow = MutableStateFlow<CurrencyRate?>(null)
    private val selectedTargetCurrencyFlow = MutableStateFlow<CurrencyRate?>(null)
    private val insertedOriginalCurrencyAmountFlow = MutableStateFlow<Int?>(null)
    private val calculatedTargetCurrencyAmountFlow = MutableStateFlow<Double?>(null)

    init {
        setupCurrencyRates()
        collectRateData()
    }

    fun onBackClicked() = viewModelScope.launch {
        eventChannel.send(CalculatorEvent.Back)
    }

    fun onOriginalAmountChanged(amount: String) {
        resetAmounts()
        insertedOriginalCurrencyAmountFlow.tryEmit(amount.getAmountFromString())
    }

    fun onOriginalCurrencyClicked() = viewModelScope.launch {
        eventChannel.send(
            CalculatorEvent.ShowOriginalCurrencyPickerDialog(
                currencyRates.map { it.shortName }
            )
        )
    }

    fun onTargetCurrencyClicked() = viewModelScope.launch {
        eventChannel.send(
            CalculatorEvent.ShowTargetCurrencyPickerDialog(
                currencyRates.map { it.shortName }
            )
        )
    }

    fun onCalculateClicked() {
        val result = calculateRateUseCase.invoke(
            selectedOriginalCurrencyFlow.value?.rate ?: return,
            selectedTargetCurrencyFlow.value?.rate ?: return,
            insertedOriginalCurrencyAmountFlow.value ?: return
        )
        calculatedTargetCurrencyAmountFlow.tryEmit(result)
        
        saveCurrencyOperation(
            CurrencyOperation( // can be created separate mapper
                date = argDate,
                originalCurrency = requireNotNull(selectedOriginalCurrencyFlow.value?.shortName),
                originalCurrencyAmount = requireNotNull(insertedOriginalCurrencyAmountFlow.value),
                targetCurrency = requireNotNull(selectedTargetCurrencyFlow.value?.shortName),
                targetCurrencyAmount = requireNotNull(calculatedTargetCurrencyAmountFlow.value)
            )
        )
    }

    fun onOriginalCurrencySelected(currency: String) = viewModelScope.launch {
        currencyRates.firstOrNull { it.shortName == currency }?.let { selectedCurrency ->
            selectedOriginalCurrencyFlow.tryEmit(selectedCurrency)
            if (selectedCurrency == selectedTargetCurrencyFlow.value) {
                selectedTargetCurrencyFlow.tryEmit(null)
            }
            resetAmounts()
        }
    }

    fun onTargetCurrencySelected(currency: String) = viewModelScope.launch {
        currencyRates.firstOrNull { it.shortName == currency }?.let { selectedCurrency ->
            selectedTargetCurrencyFlow.tryEmit(selectedCurrency)
            if (selectedCurrency == selectedOriginalCurrencyFlow.value) {
                selectedOriginalCurrencyFlow.tryEmit(null)
            }
            resetAmounts()
        }
    }

    private fun setupCurrencyRates() = viewModelScope.launch {
        currencyRatesGetUseCase.invoke(argDate).fold(
            onSuccess = {
                currencyRates = it // handle empty list
            },
            onFailure = {
                // handle error
            }
        )
        mutableViewStateFlow.update { it.copy(isLoadingVisible = false) }
    }

    private fun collectRateData() = viewModelScope.launch {
        combine(
            selectedOriginalCurrencyFlow,
            selectedTargetCurrencyFlow,
            insertedOriginalCurrencyAmountFlow,
            calculatedTargetCurrencyAmountFlow
        ) { selectedOriginalCurrency,
            selectedTargetCurrency,
            insertedOriginalCurrencyAmount,
            calculatedTargetCurrencyAmount ->
            mutableViewStateFlow.update {
                it.copy(
                    originalCurrency = selectedOriginalCurrency?.shortName,
                    targetCurrency = selectedTargetCurrency?.shortName,
                    originalCurrencyAmount = insertedOriginalCurrencyAmount?.toString() ?: "",
                    targetCurrencyAmount = calculatedTargetCurrencyAmount?.formatCalculatedAmount()
                        ?: ""
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun Double.formatCalculatedAmount() = String.format("%.2f", this) // can be use case

    private fun String.getAmountFromString(): Int? = try {
        this.replace(",", "")
            .replace(".", "")
            .toInt()
    } catch (e: Exception) {
        null
    }

    private fun resetAmounts() {
        insertedOriginalCurrencyAmountFlow.tryEmit(null)
        calculatedTargetCurrencyAmountFlow.tryEmit(null)
    }

    private fun saveCurrencyOperation(operation: CurrencyOperation) = viewModelScope.launch {
        currencyOperationSaveUseCase.invoke(operation)
    }
}