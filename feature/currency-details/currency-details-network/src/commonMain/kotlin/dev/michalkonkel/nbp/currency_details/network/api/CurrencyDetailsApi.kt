package dev.michalkonkel.nbp.currency_details.network.api

import dev.michalkonkel.nbp.currency_details.network.models.CurrencyRateDto

/**
 * Public API interface for currency details operations.
 * Provides methods to fetch detailed currency and historical rate data.
 */
interface CurrencyDetailsApi {
    /**
     * Fetches historical exchange rates for a specific currency.
     *
     * @param code ISO 4217 currency code (e.g., "USD", "EUR")
     * @param tableType NBP table type ("a", "b", or "c")
     * @param days Number of recent days to fetch (max 255)
     * @return CurrencyRateDto containing historical exchange rates
     */
    suspend fun getCurrencyRatesLastDays(
        code: String,
        tableType: String,
        days: Int,
    ): CurrencyRateDto
}
