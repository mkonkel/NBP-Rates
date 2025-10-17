package pl.nbp.domain.repository

import pl.nbp.domain.model.Currency
import pl.nbp.domain.model.CurrencyRate

/**
 * Repository interface for fetching currency data from NBP API.
 * Provides access to current exchange rates for all available currencies.
 */
interface CurrencyRepository {
    /**
     * Fetches all currencies from a specific NBP exchange rate table.
     *
     * @param table NBP table type ("A", "B", or "C"). Defaults to "A".
     * @return Result containing list of currencies or error if fetching fails
     */
    suspend fun getCurrencies(table: String = "A"): Result<List<Currency>>
}

/**
 * Repository interface for fetching historical exchange rate data.
 * Provides access to historical rates for a specific currency.
 */
interface ExchangeRateRepository {
    /**
     * Fetches historical exchange rates for a specific currency.
     *
     * @param code ISO 4217 currency code (e.g., "USD")
     * @param days Number of recent days to fetch. Defaults to 30.
     * @return Result containing list of historical rates or error if fetching fails
     */
    suspend fun getCurrencyHistory(
        code: String,
        days: Int = 30,
    ): Result<List<CurrencyRate>>
}
