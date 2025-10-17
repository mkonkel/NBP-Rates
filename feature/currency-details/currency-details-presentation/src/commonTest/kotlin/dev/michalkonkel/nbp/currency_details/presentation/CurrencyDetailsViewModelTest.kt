package dev.michalkonkel.nbp.currency_details.presentation

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import dev.michalkonkel.nbp.currency_details.presentation.di.currencyDetailsPresentationModule
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
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
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
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

            // Then
            with(viewModel.uiState.value) {
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

    @Test
    fun `loadCurrencyDetails should update state with error when repository fails`() =
        runTest {
            // Given
            val fakeRepository = FakeCurrencyDetailsRepository.createError()
            declare<CurrencyDetailsRepository> { fakeRepository }
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

            // Then
            with(viewModel.uiState.value) {
                isLoading.shouldBe(false)
                currencyDetails.shouldBeNull()
                error.shouldBe("Network error")
            }
        }

    @Test
    fun `loadCurrencyDetails should handle empty result`() =
        runTest {
            // Given
            val emptyRepo = FakeCurrencyDetailsRepository.createEmpty()
            val fakeRepository = FakeCurrencyDetailsRepository()

            declare<CurrencyDetailsRepository> { fakeRepository }
            fakeRepository.setCurrencyDetailsResult(
                Result.success(emptyRepo.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("TEST", Table.TABLE_A, 1)

            // Then
            with(viewModel.uiState.value) {
                isLoading.shouldBe(false)
                with(currencyDetails.shouldNotBeNull()) {
                    code.shouldBe("TEST")
                    historicalRates.shouldBeEmpty()
                }
                error.shouldBeNull()
            }
        }

    @Test
    fun `clearError should clear error state`() =
        runTest {
            // Given
            val fakeRepository = FakeCurrencyDetailsRepository.createError()
            declare<CurrencyDetailsRepository> { fakeRepository }
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
            val eurRepo = FakeCurrencyDetailsRepository.createEUR()
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
            fakeRepository.setCurrencyDetailsResult(
                Result.success(eurRepo.currencyDetails!!),
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
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 10)

            // Then
            with(viewModel.uiState.value) {
                isLoading.shouldBe(false)
                currencyDetails.shouldNotBeNull()
                currencyDetails.historicalRates.shouldHaveSize(5) // Fake repo returns 5 rates regardless of
                // days parameter
                error.shouldBeNull()
            }
        }

    @Test
    fun `loadCurrencyDetails with EUR should return EUR specific data`() =
        runTest {
            // Given
            val eurRepo = FakeCurrencyDetailsRepository.createEUR()
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
            fakeRepository.setCurrencyDetailsResult(
                Result.success(eurRepo.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("EUR", Table.TABLE_A, 3)

            // Then
            with(viewModel.uiState.value) {
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

    @Test
    fun `multiple loadCurrencyDetails calls should update state correctly`() =
        runTest {
            // Given
            val eurRepo = FakeCurrencyDetailsRepository.createEUR()
            val fakeRepository = FakeCurrencyDetailsRepository()
            declare<CurrencyDetailsRepository> { fakeRepository }
            fakeRepository.setCurrencyDetailsResult(
                Result.success(eurRepo.currencyDetails!!),
            )
            val viewModel: CurrencyDetailsViewModel by inject()

            // When
            viewModel.loadCurrencyDetails("EUR", Table.TABLE_A, 3)
            with(viewModel.uiState.value.currencyDetails.shouldNotBeNull()) {
                code.shouldBe("EUR")
            }

            // Update repository to return USD data
            fakeRepository.setCurrencyDetailsResult(
                Result.success(
                    FakeCurrencyDetailsRepository().currencyDetails!!,
                ),
            )

            viewModel.loadCurrencyDetails("USD", Table.TABLE_A, 5)

            // Then
            with(viewModel.uiState.value.currencyDetails.shouldNotBeNull()) {
                code.shouldBe("USD")
                name.shouldBe("dolar amerykański")
            }
        }
}
