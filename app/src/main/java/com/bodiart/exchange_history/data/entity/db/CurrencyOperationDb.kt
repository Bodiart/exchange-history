package com.bodiart.exchange_history.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENCY_OPERATION_TABLE_NAME = "history_CURRENCY_OPERATION_TABLE_NAME"

@Entity(tableName = CURRENCY_OPERATION_TABLE_NAME)
data class CurrencyOperationDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "original_currency") val originalCurrency: String,
    @ColumnInfo(name = "original_currency_amount") val originalCurrencyAmount: Int,
    @ColumnInfo(name = "target_currency") val targetCurrency: String,
    @ColumnInfo(name = "target_currency_amount") val targetCurrencyAmount: Double,
)