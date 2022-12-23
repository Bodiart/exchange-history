package com.bodiart.exchange_history.data.di.module

import com.bodiart.exchange_history.data.api.CurrencyService
import com.bodiart.exchange_history.data.di.DI_NAMED_BASE_URL
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Named(DI_NAMED_BASE_URL)
    fun provideBaseUrl(): String = "https://bank.gov.ua/"

    @Provides
    fun provideRetrofit(
        @Named(DI_NAMED_BASE_URL) baseUrl: String,
        gson: Gson
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideContactsService(retrofit: Retrofit): CurrencyService =
        retrofit.create(CurrencyService::class.java)
}