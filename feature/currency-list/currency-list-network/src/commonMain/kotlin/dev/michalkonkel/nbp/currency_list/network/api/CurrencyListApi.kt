package dev.michalkonkel.nbp.currency_list.network.api

import dev.michalkonkel.nbp.currency_list.network.models.TableDto

/**
 * Public API interface for currency list operations.
 * Provides methods to fetch currency exchange rate data.
 */
interface CurrencyListApi {
    /**
     * Fetches current exchange rates table from NBP API for specified table type.
     * @param tableType The table type ("a", "b", or "c")
     */
    suspend fun getCurrentRates(tableType: String): List<TableDto>
}
