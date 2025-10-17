package dev.michalkonkel.nbp.currency_details.presentation

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.HistoricalRate
import dev.michalkonkel.nbp.currency_details.usecase.LoadCurrencyDetailsUseCase

/**
 * Fake implementation of LoadCurrencyDetailsUseCase for testing purposes.
 * Provides static test data that can be controlled in unit tests.
 */
class FakeLoadCurrencyDetailsUseCase(
    private var detailsResult: Result<CurrencyDetails> = Result.success(
        CurrencyDetails(
            name = "dolar amerykański",
            code = "USD",
            currentRate = 4.1234,
            table = Table.TABLE_A,
            effectiveDate = "2024-01-15",
            historicalRates =
                listOf(
                    HistoricalRate("2024-01-15", 4.1234, false),
                    HistoricalRate("2024-01-14", 4.1156, false),
                    HistoricalRate("2024-01-13", 4.1098, false),
                    HistoricalRate("2024-01-12", 4.1234, false),
                    HistoricalRate("2024-01-11", 4.1345, false),
                )
        ),
    ),
) : LoadCurrencyDetailsUseCase {
    val currencyDetails: CurrencyDetails?
        get() = detailsResult.getOrNull()

    override suspend fun invoke(
        code: String,
        table: Table,
        days: Int
    ): Result<CurrencyDetails> {
        return detailsResult
    }

    /**
     * Helper method to set the result that will be returned by invoke.
     * Useful for testing different scenarios (success, error, empty data).
     */
    fun setCurrencyDetailsResult(result: Result<CurrencyDetails>) {
        detailsResult = result
    }

    companion object {
        fun createEmpty() =
            FakeLoadCurrencyDetailsUseCase(
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
            FakeLoadCurrencyDetailsUseCase(
                Result.failure(Exception("Network error")),
            )

        fun createEUR() =
            FakeLoadCurrencyDetailsUseCase(
                Result.success(
                    CurrencyDetails(
                        name = "euro",
                        code = "EUR",
                        currentRate = 4.5678,
                        table = Table.TABLE_A,
                        effectiveDate = "2024-01-15",
                        historicalRates =
                            listOf(
                                HistoricalRate(
                                    "2024-01-15",
                                    4.5678,
                                    false,
                                ),
                                HistoricalRate(
                                    "2024-01-14",
                                    4.5523,
                                    false,
                                ),
                                HistoricalRate(
                                    "2024-01-13",
                                    4.5432,
                                    false,
                                ),
                            ),
                    ),
                ),
            )

    }
}
