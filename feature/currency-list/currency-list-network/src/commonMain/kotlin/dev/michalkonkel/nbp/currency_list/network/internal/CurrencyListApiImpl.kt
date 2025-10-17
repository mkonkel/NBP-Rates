package dev.michalkonkel.nbp.currency_list.network.internal

import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi
import dev.michalkonkel.nbp.currency_list.network.models.CurrencyDto
import dev.michalkonkel.nbp.currency_list.network.models.TableDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Internal implementation of CurrencyListApi.
 * Handles communication with NBP API for currency list data.
 */
internal class CurrencyListApiImpl(
    private val httpClient: HttpClient,
) : CurrencyListApi {
    override suspend fun getCurrentRates(): List<TableDto> {
        return httpClient.get("exchangerates/tables/a").body()
    }
}
