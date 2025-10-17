package pl.nbp.api.models

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing NBP exchange rate table response.
 * Maps to the JSON structure from /api/exchangerates/tables/{table} endpoint.
 *
 * @property table Table type ("A", "B", or "C")
 * @property no Table number identifier (e.g., "001/A/NBP/2025")
 * @property effectiveDate Date when rates are effective (YYYY-MM-DD format)
 * @property rates List of currencies with their exchange rates
 */
@Serializable
data class TableDto(
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<CurrencyDto>,
)

/**
 * Data Transfer Object representing a single currency within NBP table.
 *
 * @property currency Full name of the currency in Polish
 * @property code ISO 4217 currency code
 * @property mid Mid-point exchange rate (null for table B which has bid/ask rates)
 */
@Serializable
data class CurrencyDto(
    val currency: String,
    val code: String,
    val mid: Double?,
)
