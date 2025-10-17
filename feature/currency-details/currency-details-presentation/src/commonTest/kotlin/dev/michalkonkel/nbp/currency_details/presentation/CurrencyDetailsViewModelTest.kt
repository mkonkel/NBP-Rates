package dev.michalkonkel.nbp.currency_details.presentation

import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import dev.michalkonkel.nbp.currency_details.presentation.di.currencyDetailsPresentationModule
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

    private val viewModel: CurrencyDetailsViewModel by inject()
    private lateinit var fakeRepository: FakeCurrencyDetailsRepository

    @BeforeTest
    fun setup() {
        fakeRepository = FakeCurrencyDetailsRepository()

        startKoin {
            modules(currencyDetailsPresentationModule)
        }
        declare<CurrencyDetailsRepository> { fakeRepository }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `initial state should be loading with null currency details`() = runTest {
        // Then
        viewModel.uiState.value.isLoading.shouldBe(true)
        viewModel.uiState.value.currencyDetails.shouldBeNull()
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencyDetails should update state with data when repository succeeds`() = runTest {
        // When
        viewModel.loadCurrencyDetails("USD", 5)

        // Then
        viewModel.uiState.value.isLoading.shouldBe(false)
        viewModel.uiState.value.currencyDetails.shouldNotBeNull()
        viewModel.uiState.value.currencyDetails!!.code.shouldBe("USD")
        viewModel.uiState.value.currencyDetails!!.name.shouldBe("dolar amerykański")
        viewModel.uiState.value.currencyDetails!!.currentRate.shouldBe(4.1234)
        viewModel.uiState.value.currencyDetails!!.effectiveDate.shouldBe("2024-01-15")
        viewModel.uiState.value.currencyDetails!!.historicalRates.shouldHaveSize(5)
        viewModel.uiState.value.currencyDetails!!.historicalRates.first().effectiveDate.shouldBe("2024-01-15")
        viewModel.uiState.value.currencyDetails!!.historicalRates.first().rate.shouldBe(4.1234)
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencyDetails should update state with error when repository fails`() = runTest {
        // Given
        fakeRepository.setCurrencyDetailsResult(Result.failure(Exception("Network error")))

        // When
        viewModel.loadCurrencyDetails("USD", 5)

        // Then
        viewModel.uiState.value.isLoading.shouldBe(false)
        viewModel.uiState.value.currencyDetails.shouldBeNull()
        viewModel.uiState.value.error.shouldBe("Network error")
    }

//    @Test
//    fun `loadCurrencyDetails should handle empty result`() = runTest {
//        // Given
//        fakeRepository.setCurrencyDetailsResult(
//            FakeCurrencyDetailsRepository.createEmpty()
//                .setCurrencyDetailsResult(Result.success(FakeCurrencyDetailsRepository.createEmpty().))
//        )
//
//        // When
//        viewModel.loadCurrencyDetails("TEST", 1)
//
//        // Then
//        viewModel.uiState.value.isLoading.shouldBe(false)
//        viewModel.uiState.value.currencyDetails.shouldNotBeNull()
//        viewModel.uiState.value.currencyDetails!!.code.shouldBe("TEST")
//        viewModel.uiState.value.currencyDetails!!.historicalRates.shouldBeEmpty()
//        viewModel.uiState.value.error.shouldBeNull()
//    }
//
//    @Test
//    fun `clearError should clear error state`() = runTest {
//        // Given
//        fakeRepository.setCurrencyDetailsResult(Result.failure(Exception("Test error")))
//        viewModel.loadCurrencyDetails("USD", 5)
//
//        // When
//        viewModel.clearError()
//
//        // Then
//        viewModel.uiState.value.error.shouldBeNull()
//    }
//
//    @Test
//    fun `loadCurrencyDetails should set loading to true during execution`() = runTest {
//        // Given
//        fakeRepository.setCurrencyDetailsResult(
//            FakeCurrencyDetailsRepository.createEUR()
//                .setCurrencyDetailsResult(Result.success(FakeCurrencyDetailsRepository.createEUR().))
//        )
//
//        // When
//        shouldNotThrowAny {
//            viewModel.loadCurrencyDetails("EUR", 3)
//        }
//
//        // Then - After completion, loading should be false
//        viewModel.uiState.value.isLoading.shouldBe(false)
//    }
//
//    @Test
//    fun `loadCurrencyDetails with different days parameter`() = runTest {
//        // When
//        viewModel.loadCurrencyDetails("USD", 10)
//
//        // Then
//        viewModel.uiState.value.isLoading.shouldBe(false)
//        viewModel.uiState.value.currencyDetails.shouldNotBeNull()
//        viewModel.uiState.value.currencyDetails!!.historicalRates.shouldHaveSize(5) // Fake repo returns 5 rates regardless of days parameter
//        viewModel.uiState.value.error.shouldBeNull()
//    }
//
//    @Test
//    fun `loadCurrencyDetails with EUR should return EUR specific data`() = runTest {
//        // Given
//        fakeRepository.setCurrencyDetailsResult(
//            FakeCurrencyDetailsRepository.createEUR()
//                .setCurrencyDetailsResult(Result.success(FakeCurrencyDetailsRepository.createEUR().))
//        )
//
//        // When
//        viewModel.loadCurrencyDetails("EUR", 3)
//
//        // Then
//        viewModel.uiState.value.isLoading.shouldBe(false)
//        viewModel.uiState.value.currencyDetails.shouldNotBeNull()
//        viewModel.uiState.value.currencyDetails!!.code.shouldBe("EUR")
//        viewModel.uiState.value.currencyDetails!!.name.shouldBe("euro")
//        viewModel.uiState.value.currencyDetails!!.currentRate.shouldBe(4.5678)
//        viewModel.uiState.value.currencyDetails!!.historicalRates.shouldHaveSize(3)
//        viewModel.uiState.value.currencyDetails!!.historicalRates.first().rate.shouldBe(4.5678)
//        viewModel.uiState.value.error.shouldBeNull()
//    }
//
//    @Test
//    fun `multiple loadCurrencyDetails calls should update state correctly`() = runTest {
//        // Given
//        fakeRepository.setCurrencyDetailsResult(
//            FakeCurrencyDetailsRepository.createEUR()
//                .setCurrencyDetailsResult(Result.success(FakeCurrencyDetailsRepository.createEUR().))
//        )
//
//        // When
//        viewModel.loadCurrencyDetails("EUR", 3)
//        viewModel.uiState.value.currencyDetails!!.code.shouldBe("EUR")
//
//        // Update repository to return USD data
//        fakeRepository.setCurrencyDetailsResult(
//            Result.success(
//                FakeCurrencyDetailsRepository().currencyDetails!!
//            )
//        )
//
//        viewModel.loadCurrencyDetails("USD", 5)
//
//        // Then
//        viewModel.uiState.value.currencyDetails!!.code.shouldBe("USD")
//        viewModel.uiState.value.currencyDetails!!.name.shouldBe("dolar amerykański")
//    }
}
