package com.bodiart.exchange_history.data.usecase

import com.bodiart.exchange_history.data.repository.CurrencyRepository
import javax.inject.Inject

class SavedCurrencyOperationsGetUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend fun invoke() = kotlin.runCatching {
        currencyRepository.getSavedCurrencyOperations()
    }
}