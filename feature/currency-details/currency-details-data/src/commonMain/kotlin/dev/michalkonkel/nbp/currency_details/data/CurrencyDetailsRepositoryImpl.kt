package dev.michalkonkel.nbp.currency_details.data

import dev.michalkonkel.nbp.currency_details.data.mapper.CurrencyDetailsMapper
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
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
                currencyDetailsApi.getCurrencyRatesLastDays(code, days)
                    .let { CurrencyDetailsMapper.mapToDomain(it) }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
