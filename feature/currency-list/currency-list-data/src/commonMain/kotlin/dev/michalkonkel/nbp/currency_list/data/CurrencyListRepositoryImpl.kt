package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.data.mapper.CurrencyListMapper
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.domain.CurrencyTable
import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi

internal class CurrencyListRepositoryImpl(
    private val currencyListApi: CurrencyListApi,
) : CurrencyListRepository {
    override suspend fun getCurrentRates(table: Table): Result<CurrencyTable> {
        return try {
            val tableDto =
                currencyListApi.getCurrentRates(table.value).firstOrNull()
                    ?: throw IllegalStateException("No table data received from NBP API")

            val currencyTable = CurrencyListMapper.mapToDomain(tableDto)
            Result.success(currencyTable)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
