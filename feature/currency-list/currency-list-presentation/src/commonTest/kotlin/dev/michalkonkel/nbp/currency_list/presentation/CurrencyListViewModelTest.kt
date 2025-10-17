package dev.michalkonkel.nbp.currency_list.presentation

import app.cash.turbine.test
import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.presentation.di.currencyListPresentationModule
import dev.michalkonkel.nbp.currency_list.presentation.usecase.LoadCurrencyRatesUseCase
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.koin.test.mock.declare
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class CurrencyListViewModelTest : KoinTest {
    @BeforeTest
    fun setup() {
        startKoin {
            modules(currencyListPresentationModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `initial state should be loading with empty currencies`() =
        runTest {
            // Given - declare use case before creating ViewModel
            val fakeUseCase = FakeLoadCurrencyRatesUseCase()
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }

            // When - creating ViewModel (which calls loadCurrencies() in init)
            val viewModel: CurrencyListViewModel by inject()

            // Then - should eventually reach success state with data
            viewModel.uiState.test {
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencies.shouldHaveSize(3)
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `loadCurrencies should update state with data when use case succeeds`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyRatesUseCase()
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }

            val viewModel: CurrencyListViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencies()

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencies.shouldHaveSize(3)
                    with(currencies.first()) {
                        code.shouldBe("USD")
                        name.shouldBe("dolar amerykański")
                        currentRate.shouldBe(4.1234)
                        table.shouldBe(Table.TABLE_A)
                    }
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `loadCurrencies should update state with error when use case fails`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyRatesUseCase.createError()
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }

            val viewModel: CurrencyListViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencies()

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencies.shouldBeEmpty()
                    error.shouldBe("Network error")
                }
            }
        }

    @Test
    fun `loadCurrencies should handle empty result`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyRatesUseCase.createEmpty()
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }

            val viewModel: CurrencyListViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencies()
                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencies.shouldBeEmpty()
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `loadCurrencies should set loading to true during execution`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyRatesUseCase()
            fakeUseCase.setCurrenciesResult(
                Result.success(
                    listOf(Currency("test", "TEST", 1.0, Table.TABLE_A)),
                ),
            )
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }

            val viewModel: CurrencyListViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state
                // When
                shouldNotThrowAny {
                    viewModel.loadCurrencies()
                }
                skipItems(1) // Skip initial loading state

                // Then - After completion, loading should be false
                awaitItem().isLoading.shouldBe(false)
            }
        }

    @Test
    fun `multiple loadCurrencies calls should update state correctly`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyRatesUseCase()
            declare<LoadCurrencyRatesUseCase> { fakeUseCase }
            fakeUseCase.setCurrenciesResult(
                Result.success(
                    listOf(Currency("test", "TEST", 1.0, Table.TABLE_A)),
                ),
            )

            val viewModel: CurrencyListViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state
                // When
                viewModel.loadCurrencies()
                skipItems(1) // Skip initial loading state


                with(awaitItem()) {
                    currencies.shouldHaveSize(1)
                }

                // Update use case to return different data
                fakeUseCase.setCurrenciesResult(
                    Result.success(
                        listOf(
                            Currency("dolar amerykański", "USD", 4.1234, Table.TABLE_A),
                            Currency("euro", "EUR", 4.5678, Table.TABLE_A),
                        ),
                    ),
                )

                viewModel.loadCurrencies()

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    currencies.shouldHaveSize(2)
                    currencies.first().code.shouldBe("USD")
                    currencies.last().code.shouldBe("EUR")
                }
            }
        }
}
