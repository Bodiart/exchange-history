package com.bodiart.exchange_history.data.repository

import com.bodiart.exchange_history.data.api.CurrencyService
import com.bodiart.exchange_history.data.db.CurrencyOperationDao
import com.bodiart.exchange_history.data.entity.data.CurrencyOperation
import com.bodiart.exchange_history.data.entity.data.CurrencyRate
import com.bodiart.exchange_history.data.mapper.CurrencyOperationMapper
import com.bodiart.exchange_history.data.mapper.CurrencyRateMapper
import javax.inject.Inject

private const val CURRENCY_OPERATION_MAX_SAVED_COUNT = 10

class CurrencyRepository @Inject constructor(
    private val currencyService: CurrencyService,
    private val currencyOperationDao: CurrencyOperationDao,
    private val currencyRateMapper: CurrencyRateMapper,
    private val currencyOperationMapper: CurrencyOperationMapper
) {

    suspend fun getCurrenciesRate(date: String): List<CurrencyRate> =
        currencyService.getCurrenciesRate(date).mapNotNull(currencyRateMapper::toData)

    suspend fun getSavedCurrencyOperations(): List<CurrencyOperation> =
        currencyOperationDao.getAll().map(currencyOperationMapper::toData)

    suspend fun saveCurrencyOperations(operation: CurrencyOperation) {
        currencyOperationDao.insert(currencyOperationMapper.toDb(operation))
        val all = currencyOperationDao.getAll()
        if (all.size > CURRENCY_OPERATION_MAX_SAVED_COUNT) { // remove old items
            val itemsToDelete = all.subList(0, all.size - CURRENCY_OPERATION_MAX_SAVED_COUNT)
            itemsToDelete.forEach {
                currencyOperationDao.deleteById(it.id) // can be optimized with one transaction
            }
        }
    }
}