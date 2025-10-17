package dev.michalkonkel.nbp.currency_details.network.models

import kotlinx.serialization.Serializable

/**
 * Data Transfer Object representing currency with historical rates from NBP API.
 *
 * @property table Table type ("A", "B", or "C")
 * @property currency Full name of the currency in Polish
 * @property code ISO 4217 currency code
 * @property rates List of historical exchange rates
 */
@Serializable
data class CurrencyRateDto(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<RateDto>,
)

/**
 * Data Transfer Object representing a single rate with date.
 *
 * @property no Table number identifier
 * @property effectiveDate Date when rate was effective
 * @param mid Mid-point exchange rate
 */
@Serializable
data class RateDto(
    val no: String,
    val effectiveDate: String,
    val mid: Double,
)
