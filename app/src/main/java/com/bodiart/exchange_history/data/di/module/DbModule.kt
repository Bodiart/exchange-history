package com.bodiart.exchange_history.data.di.module

import android.content.Context
import androidx.room.Room
import com.bodiart.exchange_history.data.db.AppDatabase
import com.bodiart.exchange_history.data.di.DI_NAMED_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class DbModule {

    @Provides
    @Named(DI_NAMED_DATABASE_NAME)
    fun provideDatabaseName() = "app_database"

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
        @Named(DI_NAMED_DATABASE_NAME) databaseName: String
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()

    @Provides
    fun providesCurrencyOperationDao(database: AppDatabase) = database.currencyOperationDao()
}