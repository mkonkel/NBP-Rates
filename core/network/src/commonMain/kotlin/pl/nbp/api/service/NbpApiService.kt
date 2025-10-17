package pl.nbp.api.service

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import pl.nbp.api.models.CurrencyRateDto
import pl.nbp.api.models.TableDto

/**
 * API service for communicating with NBP (Narodowy Bank Polski) exchange rate API.
 * Provides methods to fetch current and historical exchange rate data.
 * 
 * @property HttpClient Configured HTTP client for making API requests
 */
class NbpApiService(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://api.nbp.pl/api/"
    }

    /**
     * Fetches current exchange rates table from NBP API.
     * 
     * @param table NBP table type ("A", "B", or "C")
     * @return List of TableDto containing exchange rate data
     * @throws Exception on network or API errors
     */
    suspend fun getExchangeRatesTable(table: String): List<TableDto> {
        return httpClient.get("${BASE_URL}exchangerates/tables/$table").body()
    }

    /**
     * Fetches historical exchange rates for a specific currency.
     * 
     * @param code ISO 4217 currency code (e.g., "USD", "EUR")
     * @param days Number of recent days to fetch (max 255)
     * @return CurrencyRateDto containing historical rates data
     * @throws Exception on network or API errors
     */
    suspend fun getCurrencyRatesLastDays(code: String, days: Int): CurrencyRateDto {
        return httpClient.get("${BASE_URL}exchangerates/rates/a/$code/last/$days").body()
    }
}