package dev.michalkonkel.nbp.currency_details.usecase

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails

/**
 * Use case interface for loading currency details with highlighting logic.
 * Defines the contract for loading and processing currency details data.
 * TODO: This should be moved to a separate use case layer, but keeping it in presentation for now.
 * TODO: Missing tests for this class.
 */
interface LoadCurrencyDetailsUseCase {
    /**
     * Loads currency details and applies highlighting logic to historical rates.
     * Rates that differ by more than 10% from the current rate will be highlighted.
     */
    suspend operator fun invoke(
        code: String,
        table: Table = Table.TABLE_A,
        days: Int = 30,
    ): Result<CurrencyDetails>
}
