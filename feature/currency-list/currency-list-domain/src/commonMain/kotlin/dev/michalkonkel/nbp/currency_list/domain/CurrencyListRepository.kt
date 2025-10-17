package dev.michalkonkel.nbp.currency_list.domain

import dev.michalkonkel.nbp.core.domain.Table

/**
 * Repository interface for currency list data.
 */
interface CurrencyListRepository {
    suspend fun getCurrentRates(table: Table): Result<CurrencyTable>
}
