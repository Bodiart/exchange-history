package com.bodiart.exchange_history.data.usecase

import javax.inject.Inject

class CalculateRateUseCase @Inject constructor() {

    fun invoke(originRate: Double, targetRate: Double, amount: Int): Double {
        return (originRate * amount.toDouble()) / targetRate
    }
}