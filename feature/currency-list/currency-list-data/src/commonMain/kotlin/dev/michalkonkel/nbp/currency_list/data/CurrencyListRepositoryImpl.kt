package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.currency_list.data.mapper.CurrencyListMapper
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.domain.CurrencyTable
import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi

internal class CurrencyListRepositoryImpl(
    private val currencyListApi: CurrencyListApi,
) : CurrencyListRepository {
    override suspend fun getCurrentRates(): Result<List<CurrencyTable>> {
        return try {
            val tableDto =
                currencyListApi.getCurrentRates()
                    .map { CurrencyListMapper.mapToDomain(it) }

            Result.success(tableDto)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
