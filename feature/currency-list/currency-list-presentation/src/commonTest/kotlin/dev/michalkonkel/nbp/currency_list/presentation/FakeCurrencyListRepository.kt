package dev.michalkonkel.nbp.currency_list.presentation

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository

/**
 * Fake implementation of CurrencyListRepository for testing purposes.
 * Provides static test data that can be controlled in unit tests.
 */
class FakeCurrencyListRepository(
    private var currenciesResult: Result<List<Currency>> =
        Result.success(
            listOf(
                Currency(
                    name = "dolar amerykański",
                    code = "USD",
                    currentRate = 4.1234,
                    table = Table.TABLE_A,
                ),
                Currency(
                    name = "euro",
                    code = "EUR",
                    currentRate = 4.5678,
                    table = Table.TABLE_A,
                ),
                Currency(
                    name = "funt szterling",
                    code = "GBP",
                    currentRate = 5.2345,
                    table = Table.TABLE_A,
                ),
            ),
        ),
) : CurrencyListRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return currenciesResult
    }

    /**
     * Helper method to set the result that will be returned by getCurrencies.
     * Useful for testing different scenarios (success, error, empty data).
     */
    fun setCurrenciesResult(result: Result<List<Currency>>) {
        currenciesResult = result
    }

    companion object {
        fun createEmpty() = FakeCurrencyListRepository(Result.success(emptyList()))

        fun createError() =
            FakeCurrencyListRepository(
                Result.failure(Exception("Network error")),
            )
    }
}
