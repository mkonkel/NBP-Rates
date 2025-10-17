package dev.michalkonkel.nbp.currency_details.domain

/**
 * Domain model for currency details screen.
 */
data class CurrencyDetails(
    val name: String,
    val code: String,
    val currentRate: Double,
    val table: String,
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
        days: Int = 30,
    ): Result<CurrencyDetails>
}
