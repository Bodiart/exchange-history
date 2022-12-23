package com.bodiart.exchange_history.data.mapper

import com.bodiart.exchange_history.data.entity.api.CurrencyRateApi
import com.bodiart.exchange_history.data.entity.data.CurrencyRate
import javax.inject.Inject

class CurrencyRateMapper @Inject constructor() {

    fun toData(api: CurrencyRateApi): CurrencyRate? {
        return CurrencyRate(
            id = api.id ?: return null, // id cant be null
            fullName = api.fullName ?: return null, // full name cant be null
            shortName = api.shortName ?: return null, // short name cant be null
            rate = api.rate ?: return null // rate cant be null
        )
    }
}