package com.bodiart.exchange_history.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bodiart.exchange_history.data.entity.db.CurrencyOperationDb

@Database(entities = [CurrencyOperationDb::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyOperationDao(): CurrencyOperationDao
}