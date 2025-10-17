package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi

internal class CurrencyListRepositoryImpl(
    private val currencyListApi: CurrencyListApi,
) : CurrencyListRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return try {
            val currencyDtos = currencyListApi.getCurrentRates()
            Result.success(
                currencyDtos.map { dto ->
                    Currency(
                        name = dto.currency,
                        code = dto.code,
                        currentRate = dto.mid ?: 0.0,
                        table = "A"
                    )
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
