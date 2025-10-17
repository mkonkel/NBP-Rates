package dev.michalkonkel.nbp.currency_list.domain

/**
 * Repository interface for currency list data.
 */
interface CurrencyListRepository {
    suspend fun getCurrencies(): Result<List<Currency>>
}
