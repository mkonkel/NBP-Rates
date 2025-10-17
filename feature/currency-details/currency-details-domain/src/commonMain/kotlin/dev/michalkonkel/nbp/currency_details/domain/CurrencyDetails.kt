package dev.michalkonkel.nbp.currency_details.domain

import dev.michalkonkel.nbp.core.domain.Table

/**
 * Domain model for currency details screen.
 */
data class CurrencyDetails(
    val name: String,
    val code: String,
    val currentRate: Double,
    val table: Table,
    val effectiveDate: String,
    val historicalRates: List<HistoricalRate> = emptyList(),
)

/**
 * Historical rate data for a currency.
 */
data class HistoricalRate(
    val effectiveDate: String,
    val rate: Double,
)

/**
 * Repository interface for currency details data.
 */
interface CurrencyDetailsRepository {
    suspend fun getCurrencyDetails(
        code: String,
        table: Table = Table.TABLE_A,
        days: Int = 30,
    ): Result<CurrencyDetails>
}
