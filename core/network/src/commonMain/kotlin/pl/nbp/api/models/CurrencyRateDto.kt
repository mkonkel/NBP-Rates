package pl.nbp.api.models

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing NBP currency rates history response.
 * Maps to the JSON structure from /api/exchangerates/rates/{table}/{code}/last/{top} endpoint.
 *
 * @property table Table type ("A", "B", or "C")
 * @property currency Full name of the currency in Polish
 * @property code ISO 4217 currency code
 * @property rates List of historical rates with dates and values
 */
@Serializable
data class CurrencyRateDto(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<RateDto>,
)

/**
 * Data Transfer Object representing a single historical exchange rate entry.
 *
 * @property no Table number identifier (e.g., "001/A/NBP/2025")
 * @property effectiveDate Date when rate was effective (YYYY-MM-DD format)
 * @property mid Mid-point exchange rate (for tables A and C)
 * @property bid Buying rate (for table B only)
 * @property ask Selling rate (for table B only)
 */
@Serializable
data class RateDto(
    val no: String,
    val effectiveDate: String,
    val mid: Double,
    val bid: Double?,
    val ask: Double?,
)
