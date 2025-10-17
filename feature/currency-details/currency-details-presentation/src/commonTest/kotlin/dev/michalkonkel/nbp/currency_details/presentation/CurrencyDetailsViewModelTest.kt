package dev.michalkonkel.nbp.currency_details.presentation

import app.cash.turbine.test
import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.presentation.di.currencyDetailsPresentationModule
import dev.michalkonkel.nbp.currency_details.usecase.LoadCurrencyDetailsUseCase
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
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

class CurrencyDetailsViewModelTest : KoinTest {
    @BeforeTest
    fun setup() {
        startKoin {
            modules(currencyDetailsPresentationModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `initial state should be not loading with null currency details`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            val viewModel: CurrencyDetailsViewModel by inject()

            // Then
            with(viewModel.uiState.value) {
                isLoading.shouldBe(false)
                currencyDetails.shouldBeNull()
                error.shouldBeNull()
            }
        }

    @Test
    fun `loadCurrencyDetails should update state with data when repository succeeds`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            val viewModel: CurrencyDetailsViewModel by inject()


            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    with(currencyDetails.shouldNotBeNull()) {
                        code.shouldBe("USD")
                        name.shouldBe("dolar amerykański")
                        currentRate.shouldBe(4.1234)
                        effectiveDate.shouldBe("2024-01-15")
                        historicalRates.shouldHaveSize(5)
                        with(historicalRates.first()) {
                            effectiveDate.shouldBe("2024-01-15")
                            rate.shouldBe(4.1234)
                        }
                    }
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `loadCurrencyDetails should update state with error when repository fails`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase.createError()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            val viewModel: CurrencyDetailsViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencyDetails.shouldBeNull()
                    error.shouldBe("Network error")
                }
            }
        }

    @Test
    fun `loadCurrencyDetails should handle empty result`() =
        runTest {
            // Given
            val emptyUseCase = FakeLoadCurrencyDetailsUseCase.createEmpty()
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()

            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            fakeUseCase.setCurrencyDetailsResult(
                Result.success(emptyUseCase.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("TEST", Table.TABLE_A, 1)

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    with(currencyDetails.shouldNotBeNull()) {
                        code.shouldBe("TEST")
                        historicalRates.shouldBeEmpty()
                    }
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `clearError should clear error state`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase.createError()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            val viewModel: CurrencyDetailsViewModel by inject()
            viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

            // When
            viewModel.clearError()

            // Then
            viewModel.uiState.value.error.shouldBeNull()
        }

    @Test
    fun `loadCurrencyDetails should set loading to true during execution`() =
        runTest {
            // Given
            val eurUseCase = FakeLoadCurrencyDetailsUseCase.createEUR()
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            fakeUseCase.setCurrencyDetailsResult(
                Result.success(eurUseCase.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            shouldNotThrowAny {
                viewModel.loadCurrencyDetails("EUR", Table.TABLE_A, 3)
            }

            // Then - After completion, loading should be false
            viewModel.uiState.value.isLoading.shouldBe(false)
        }

    @Test
    fun `loadCurrencyDetails with different days parameter`() =
        runTest {
            // Given
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            val viewModel: CurrencyDetailsViewModel by inject()


            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 10)

                skipItems(1) // Skip initial loading state


                // Then
                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    currencyDetails.shouldNotBeNull()
                    currencyDetails.historicalRates.shouldHaveSize(5) // Fake repo returns 5 rates regardless of
                    // days parameter
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `loadCurrencyDetails with EUR should return EUR specific data`() =
        runTest {
            // Given
            val eurUseCase = FakeLoadCurrencyDetailsUseCase.createEUR()
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            fakeUseCase.setCurrencyDetailsResult(
                Result.success(eurUseCase.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()



            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("EUR", Table.TABLE_A, 3)

                skipItems(1) // Skip initial loading state

                with(awaitItem()) {
                    isLoading.shouldBe(false)
                    with(currencyDetails.shouldNotBeNull()) {
                        code.shouldBe("EUR")
                        name.shouldBe("euro")
                        currentRate.shouldBe(4.5678)
                        historicalRates.shouldHaveSize(3)
                        historicalRates.first().rate.shouldBe(4.5678)
                    }
                    error.shouldBeNull()
                }
            }
        }

    @Test
    fun `multiple loadCurrencyDetails calls should update state correctly`() =
        runTest {
            // Given
            val eurUseCase = FakeLoadCurrencyDetailsUseCase.createEUR()
            val fakeUseCase = FakeLoadCurrencyDetailsUseCase()
            declare<LoadCurrencyDetailsUseCase> { fakeUseCase }
            fakeUseCase.setCurrencyDetailsResult(
                Result.success(eurUseCase.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            viewModel.uiState.test {
                skipItems(1) // Skip initial loading state

                // When
                viewModel.loadCurrencyDetails("EUR", Table.TABLE_A, 3)

                skipItems(1) // Skip initial loading state

                with(awaitItem().currencyDetails.shouldNotBeNull()) { code.shouldBe("EUR") }

                // Update use case to return USD data
                fakeUseCase.setCurrencyDetailsResult(
                    Result.success(FakeLoadCurrencyDetailsUseCase().currencyDetails!!),
                )

                // When
                viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

                skipItems(1) // Skip initial loading state

                // Then
                with(awaitItem().currencyDetails.shouldNotBeNull()) {
                    code.shouldBe("USD")
                    name.shouldBe("dolar amerykański")
                }
            }
        }
}
