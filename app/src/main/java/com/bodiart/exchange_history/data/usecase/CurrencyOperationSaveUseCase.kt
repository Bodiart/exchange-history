package com.bodiart.exchange_history.data.usecase

import com.bodiart.exchange_history.data.entity.data.CurrencyOperation
import com.bodiart.exchange_history.data.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyOperationSaveUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {

    suspend fun invoke(operation: CurrencyOperation) =
        currencyRepository.saveCurrencyOperations(operation)
}