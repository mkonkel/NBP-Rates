package dev.michalkonkel.nbp.currency_list.presentation.usecase

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * Use case implementation for loading currency rates from tables A and B in parallel.
 * TODO: This should be moved to a separate use case layer, but keeping it in presentation for now.
 * TODO: Missing tests for this class.
 */
internal class LoadCurrencyRatesUseCaseImpl(
    private val currencyListRepository: CurrencyListRepository,
) : LoadCurrencyRatesUseCase {
    /**
     * Loads currency rates from tables A and B in parallel and combines them.
     * Filters out table C as requested and sorts currencies by effective date (newest first).
     */
    override suspend operator fun invoke(): Result<List<Currency>> =
        try {
            coroutineScope {
                // Make parallel repository calls for tables A and B
                val tableADeferred = async { currencyListRepository.getCurrentRates(Table.TABLE_A) }
                val tableBDeferred = async { currencyListRepository.getCurrentRates(Table.TABLE_B) }

                // Wait for both calls to complete
                val (tableA, tableB) =
                    awaitAll(tableADeferred, tableBDeferred)
                        .map { it.getOrThrow() }

                // Combine currencies from both tables and sort by effective date (newest first)
                val allCurrencies =
                    (tableA.rates + tableB.rates)
                        .sortedByDescending { tableA.effectiveDate } // Sort by date (newest first)

                Result.success(allCurrencies)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
}
