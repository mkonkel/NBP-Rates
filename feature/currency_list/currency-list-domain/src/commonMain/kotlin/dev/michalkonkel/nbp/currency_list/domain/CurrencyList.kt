package dev.michalkonkel.nbp.currency_list.domain

/**
 * Domain model for currency list screen.
 */
data class CurrencyList(
    val currencies: List<Currency>,
    val isLoading: Boolean,
    val error: String?
)

/**
 * Currency entity for display purposes.
 */
data class Currency(
    val name: String,
    val code: String,
    val currentRate: Double,
    val table: String
)

/**
 * Repository interface for currency list data.
 */
interface CurrencyListRepository {
    suspend fun getCurrencies(): Result<List<Currency>>
}