package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi
import dev.michalkonkel.nbp.currency_list.data.mapper.CurrencyListMapper

internal class CurrencyListRepositoryImpl(
    private val currencyListApi: CurrencyListApi,
) : CurrencyListRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return try {
            val currencyDtos = currencyListApi.getCurrentRates()
            Result.success(CurrencyListMapper.mapToDomain(currencyDtos))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
