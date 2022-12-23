package com.bodiart.exchange_history.data.usecase

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DATE_PATTERN = "yyyyMMdd"

class CurrencyRatesBuildDateUseCase @Inject constructor() {

    fun invoke(date: LocalDate): String =
        date.format(DateTimeFormatter.ofPattern(DATE_PATTERN))
}