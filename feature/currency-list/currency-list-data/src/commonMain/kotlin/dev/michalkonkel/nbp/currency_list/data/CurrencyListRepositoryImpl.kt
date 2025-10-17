package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi

internal class CurrencyListRepositoryImpl(
    private val currencyListApi: CurrencyListApi,
) : CurrencyListRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return try {
            val result =
                currencyListApi.getCurrentRates()
                    .map { dtos ->
                        TODO()
                    }
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
