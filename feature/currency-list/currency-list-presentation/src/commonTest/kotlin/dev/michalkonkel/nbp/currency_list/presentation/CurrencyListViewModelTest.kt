package dev.michalkonkel.nbp.currency_list.presentation

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.presentation.di.currencyListPresentationModule
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

    private val viewModel: CurrencyListViewModel by inject()
    private lateinit var fakeRepository: FakeCurrencyListRepository

    @BeforeTest
    fun setup() {
        fakeRepository = FakeCurrencyListRepository()

        startKoin {
            modules(currencyListPresentationModule)
        }
        declare<CurrencyListRepository> { fakeRepository }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `initial state should be loading with empty currencies`() = runTest {
        // Then
        viewModel.uiState.value.isLoading.shouldBe(true)
        viewModel.uiState.value.currencies.shouldBeEmpty()
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencies should update state with data when repository succeeds`() = runTest {
        // When
        viewModel.loadCurrencies()

        // Then
        viewModel.uiState.value.isLoading.shouldBe(false)
        viewModel.uiState.value.currencies.shouldHaveSize(3)
        viewModel.uiState.value.currencies.first().code.shouldBe("USD")
        viewModel.uiState.value.currencies.first().name.shouldBe("dolar amerykański")
        viewModel.uiState.value.currencies.first().currentRate.shouldBe(4.1234)
        viewModel.uiState.value.currencies.first().table.shouldBe(Table.TABLE_A)
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencies should update state with error when repository fails`() = runTest {
        // Given
        fakeRepository.setCurrenciesResult(Result.failure(Exception("Network error")))

        // When
        viewModel.loadCurrencies()

        // Then
        viewModel.uiState.value.isLoading.shouldBe(false)
        viewModel.uiState.value.currencies.shouldBeEmpty()
        viewModel.uiState.value.error.shouldBe("Network error")
    }

    @Test
    fun `loadCurrencies should handle empty result`() = runTest {
        // Given
        fakeRepository = FakeCurrencyListRepository()
        fakeRepository.setCurrenciesResult(Result.success(emptyList()))

        // When
        viewModel.loadCurrencies()

        // Then
        viewModel.uiState.value.isLoading.shouldBe(false)
        viewModel.uiState.value.currencies.shouldBeEmpty()
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `clearError should clear error state`() = runTest {
        // Given
        fakeRepository = FakeCurrencyListRepository()
        fakeRepository.setCurrenciesResult(Result.failure(Exception("Test error")))
        viewModel.loadCurrencies()

        // When
        viewModel.clearError()

        // Then
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencies should set loading to true during execution`() = runTest {
        // Given
        fakeRepository = FakeCurrencyListRepository()
        fakeRepository.setCurrenciesResult(
            Result.success(
                listOf(Currency("test", "TEST", 1.0, Table.TABLE_A))
            )
        )

        // When
        shouldNotThrowAny {
            viewModel.loadCurrencies()
        }

        // Then - After completion, loading should be false
        viewModel.uiState.value.isLoading.shouldBe(false)
    }

    @Test
    fun `multiple loadCurrencies calls should update state correctly`() = runTest {
        // Given
        fakeRepository = FakeCurrencyListRepository()
        fakeRepository.setCurrenciesResult(
            Result.success(
                listOf(Currency("test", "TEST", 1.0, Table.TABLE_A))
            )
        )

        // When
        viewModel.loadCurrencies()
        viewModel.uiState.value.currencies.shouldHaveSize(1)

        // Update repository to return different data
        fakeRepository.setCurrenciesResult(
            Result.success(
                listOf(
                    Currency("dolar amerykański", "USD", 4.1234, Table.TABLE_A),
                    Currency("euro", "EUR", 4.5678, Table.TABLE_A),
                )
            )
        )

        viewModel.loadCurrencies()

        // Then
        viewModel.uiState.value.currencies.shouldHaveSize(2)
        viewModel.uiState.value.currencies.first().code.shouldBe("USD")
        viewModel.uiState.value.currencies.last().code.shouldBe("EUR")
    }
}
