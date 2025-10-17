package dev.michalkonkel.nbp.currency_details.presentation

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import dev.michalkonkel.nbp.currency_details.domain.HistoricalRate

/**
 * Fake implementation of CurrencyDetailsRepository for testing purposes.
 * Provides static test data that can be controlled in unit tests.
 */
class FakeCurrencyDetailsRepository(
    private var detailsResult: Result<CurrencyDetails> =
        Result.success(
            CurrencyDetails(
                name = "dolar amerykański",
                code = "USD",
                currentRate = 4.1234,
                table = Table.TABLE_A,
                effectiveDate = "2024-01-15",
                historicalRates =
                    listOf(
                        HistoricalRate("2024-01-15", 4.1234),
                        HistoricalRate("2024-01-14", 4.1156),
                        HistoricalRate("2024-01-13", 4.1098),
                        HistoricalRate("2024-01-12", 4.1234),
                        HistoricalRate("2024-01-11", 4.1345),
                    ),
            ),
        ),
) : CurrencyDetailsRepository {
    val currencyDetails: CurrencyDetails?
        get() = detailsResult.getOrNull()

    override suspend fun getCurrencyDetails(
        code: String,
        table: Table,
        days: Int,
    ): Result<CurrencyDetails> {
        return detailsResult
    }

    /**
     * Helper method to set the result that will be returned by getCurrencyDetails.
     * Useful for testing different scenarios (success, error, empty data).
     */
    fun setCurrencyDetailsResult(result: Result<CurrencyDetails>) {
        detailsResult = result
    }

    companion object {
        fun createEmpty() =
            FakeCurrencyDetailsRepository(
                Result.success(
                    CurrencyDetails(
                        name = "test",
                        code = "TEST",
                        currentRate = 0.0,
                        table = Table.TABLE_A,
                        effectiveDate = "2024-01-15",
                        historicalRates = emptyList(),
                    ),
                ),
            )

        fun createError() =
            FakeCurrencyDetailsRepository(
                Result.failure(Exception("Network error")),
            )

        fun createEUR() =
            FakeCurrencyDetailsRepository(
                Result.success(
                    CurrencyDetails(
                        name = "euro",
                        code = "EUR",
                        currentRate = 4.5678,
                        table = Table.TABLE_A,
                        effectiveDate = "2024-01-15",
                        historicalRates =
                            listOf(
                                HistoricalRate("2024-01-15", 4.5678),
                                HistoricalRate("2024-01-14", 4.5523),
                                HistoricalRate("2024-01-13", 4.5432),
                            ),
                    ),
                ),
            )
    }
}
