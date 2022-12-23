package com.bodiart.exchange_history.data.usecase

import com.bodiart.exchange_history.data.repository.CurrencyRepository
import javax.inject.Inject

class CurrencyRatesGetUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend fun invoke(date: String) = kotlin.runCatching {
        currencyRepository.getCurrenciesRate(date)
    }
}