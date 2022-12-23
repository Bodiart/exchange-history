package com.bodiart.exchange_history.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bodiart.exchange_history.data.entity.db.CURRENCY_OPERATION_TABLE_NAME
import com.bodiart.exchange_history.data.entity.db.CurrencyOperationDb

@Dao
interface CurrencyOperationDao {

    @Query("SELECT * FROM $CURRENCY_OPERATION_TABLE_NAME")
    suspend fun getAll(): List<CurrencyOperationDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(operation: CurrencyOperationDb)

    @Query("DELETE FROM $CURRENCY_OPERATION_TABLE_NAME WHERE id=:id")
    suspend fun deleteById(id: Int)
}