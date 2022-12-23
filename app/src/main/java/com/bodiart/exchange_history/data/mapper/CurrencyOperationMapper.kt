package com.bodiart.exchange_history.data.mapper

import com.bodiart.exchange_history.data.entity.data.CurrencyOperation
import com.bodiart.exchange_history.data.entity.db.CurrencyOperationDb
import java.util.Date
import javax.inject.Inject

class CurrencyOperationMapper @Inject constructor() {

    fun toDb(operation: CurrencyOperation): CurrencyOperationDb {
        return CurrencyOperationDb(
            date = operation.date,
            originalCurrency = operation.originalCurrency,
            originalCurrencyAmount = operation.originalCurrencyAmount,
            targetCurrency = operation.targetCurrency,
            targetCurrencyAmount = operation.targetCurrencyAmount
        )
    }

    fun toData(db: CurrencyOperationDb): CurrencyOperation {
        return CurrencyOperation(
            date = db.date,
            originalCurrency = db.originalCurrency,
            originalCurrencyAmount = db.originalCurrencyAmount,
            targetCurrency = db.targetCurrency,
            targetCurrencyAmount = db.targetCurrencyAmount
        )
    }
}