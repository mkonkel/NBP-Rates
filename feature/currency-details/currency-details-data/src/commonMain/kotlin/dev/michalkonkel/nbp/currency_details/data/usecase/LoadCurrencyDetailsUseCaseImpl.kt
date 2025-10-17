package dev.michalkonkel.nbp.currency_details.domain.usecase

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import kotlin.math.abs

/**
 * Use case implementation for loading currency details with highlighting logic.
 * Handles repository communication and applies business rules for highlighting historical rates.
 */
internal class LoadCurrencyDetailsUseCaseImpl(
    private val currencyDetailsRepository: CurrencyDetailsRepository,
) : LoadCurrencyDetailsUseCase {
    companion object {
        private const val HIGHLIGHT_THRESHOLD_PERCENTAGE = 0.10 // 10%
    }

    /**
     * Loads currency details and applies highlighting logic to historical rates.
     * Rates that differ by more than 10% from the current rate will be highlighted.
     */
    override suspend operator fun invoke(
        code: String,
        table: Table,
        days: Int,
    ): Result<CurrencyDetails> =
        try {
            // Fetch data from repository
            val result = currencyDetailsRepository.getCurrencyDetails(code, table, days).getOrThrow()

            // Apply highlighting logic to historical rates
            val currencyDetailsWithHighlighting =
                result.copy(
                    historicalRates =
                        result.historicalRates.map { historicalRate ->
                            val isHighlighted = shouldHighlight(historicalRate.rate, result.currentRate)
                            historicalRate.copy(isHighlighted = isHighlighted)
                        },
                )

            Result.success(currencyDetailsWithHighlighting)
        } catch (e: Exception) {
            Result.failure(e)
        }

    /**
     * Determines if a historical rate should be highlighted based on 10% threshold.
     */
    private fun shouldHighlight(
        historicalRate: Double,
        currentRate: Double,
    ): Boolean {
        if (currentRate == 0.0) return false

        val percentageDifference = abs(historicalRate - currentRate) / currentRate
        return percentageDifference > HIGHLIGHT_THRESHOLD_PERCENTAGE
    }
}
