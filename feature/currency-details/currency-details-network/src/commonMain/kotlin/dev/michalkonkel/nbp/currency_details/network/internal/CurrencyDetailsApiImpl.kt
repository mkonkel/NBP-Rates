package dev.michalkonkel.nbp.currency_details.network.internal

import dev.michalkonkel.nbp.currency_details.network.api.CurrencyDetailsApi
import dev.michalkonkel.nbp.currency_details.network.models.CurrencyRateDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

/**
 * Internal implementation of CurrencyDetailsApi.
 * Handles communication with NBP API for currency details and historical data.
 */
internal class CurrencyDetailsApiImpl(
    private val httpClient: HttpClient,
) : CurrencyDetailsApi {
    override suspend fun getCurrencyRatesLastDays(
        code: String,
        days: Int,
    ): CurrencyRateDto {
        return httpClient.get("exchangerates/rates/a/$code/last/$days").body()
    }
}
