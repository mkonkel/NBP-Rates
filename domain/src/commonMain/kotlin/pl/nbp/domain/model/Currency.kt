package pl.nbp.domain.model

/**
 * Represents a currency with its current exchange rate.
 *
 * @property name Full name of the currency (e.g., "dolar amerykański")
 * @property code ISO 4217 currency code (e.g., "USD")
 * @property currentRate Current exchange rate relative to PLN
 * @property table NBP exchange rate table type ("A", "B", or "C")
 */
data class Currency(
    val name: String,
    val code: String,
    val currentRate: Double,
    val table: String,
)

/**
 * Represents historical exchange rate data for a specific currency.
 *
 * @property code ISO 4217 currency code
 * @property rate Exchange rate value for the given date
 * @property date Date of the exchange rate in "YYYY-MM-DD" format
 * @property tableNumber NBP table number identifier (e.g., "001/A/NBP/2025")
 */
data class CurrencyRate(
    val code: String,
    val rate: Double,
    val date: String,
    val tableNumber: String,
)
