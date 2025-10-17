package dev.michalkonkel.nbp.currency_details.data

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import dev.michalkonkel.nbp.currency_details.domain.HistoricalRate
import dev.michalkonkel.nbp.currency_details.network.api.CurrencyDetailsApi

internal class CurrencyDetailsRepositoryImpl(
    private val currencyDetailsApi: CurrencyDetailsApi,
) : CurrencyDetailsRepository {
    override suspend fun getCurrencyDetails(
        code: String,
        days: Int,
    ): Result<CurrencyDetails> {
        return try {
            val result =
                currencyDetailsApi.getCurrencyRatesLastDays(code, days).let { dto ->
                    CurrencyDetails(
                        name = dto.currency,
                        code = dto.code,
                        currentRate = dto.rates.firstOrNull()?.mid ?: 0.0,
                        table = Table.fromString(dto.table),
                        effectiveDate = dto.rates.firstOrNull()?.effectiveDate ?: "",
                        historicalRates =
                            dto.rates.map { rate ->
                                HistoricalRate(
                                    effectiveDate = rate.effectiveDate,
                                    rate = rate.mid,
                                )
                            },
                    )
                }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
