package dev.michalkonkel.nbp.currency_list.presentation.usecase

import dev.michalkonkel.nbp.currency_list.domain.Currency

/**
 * Use case interface for loading currency rates from tables A and B in parallel.
 * Defines the contract for loading and processing currency data.
 */
interface LoadCurrencyRatesUseCase {
    /**
     * Loads currency rates from tables A and B in parallel and combines them.
     * Filters out table C as requested and sorts currencies by effective date (newest first).
     */
    suspend operator fun invoke(): Result<List<Currency>>
}
