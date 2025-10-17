package dev.michalkonkel.nbp.currency_list.network

import dev.michalkonkel.nbp.core.network.HttpClientFactory
import dev.michalkonkel.nbp.currency_list.network.models.CurrencyDto
import dev.michalkonkel.nbp.currency_list.network.models.TableDto
import io.ktor.client.call.body
import io.ktor.client.request.get

class CurrencyListNetworkService(
    private val httpClientFactory: HttpClientFactory
) {
    suspend fun getCurrentRates(): Result<List<CurrencyDto>> {
        return try {
            val httpClient = httpClientFactory.create()
            val response = httpClient.get("https://api.nbp.pl/api/exchangerates/tables/a")
            val table: TableDto = response.body()
            Result.success(table.rates)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}