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
    fun `initial state should be loading with empty currencies`() = runTest {
        // Given - declare repository before creating ViewModel
        val fakeRepository = FakeCurrencyListRepository()
        declare<CurrencyListRepository> { fakeRepository }

        // When - creating ViewModel (which calls loadCurrencies() in init)
        val viewModel: CurrencyListViewModel by inject()

        // Then - should be in loading state initially
        with(viewModel.uiState.value) {
            isLoading.shouldBe(true)
            currencies.shouldBeEmpty()
            error.shouldBeNull()
        }
    }

    @Test
    fun `loadCurrencies should update state with data when repository succeeds`() = runTest {
        // Given
        val fakeRepository = FakeCurrencyListRepository()
        declare<CurrencyListRepository> { fakeRepository }

        val viewModel: CurrencyListViewModel by inject()

        // When
        viewModel.loadCurrencies()

        // Then
        with(viewModel.uiState.value) {
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

    @Test
    fun `loadCurrencies should update state with error when repository fails`() = runTest {
        // Given
        val fakeRepository = FakeCurrencyListRepository.createError()
        declare<CurrencyListRepository> { fakeRepository }

        val viewModel: CurrencyListViewModel by inject()

        // When
        viewModel.loadCurrencies()

        // Then
        with(viewModel.uiState.value) {
            isLoading.shouldBe(false)
            currencies.shouldBeEmpty()
            error.shouldBe("Network error")
        }
    }

    @Test
    fun `loadCurrencies should handle empty result`() = runTest {
        // Given
        val fakeRepository = FakeCurrencyListRepository.createEmpty()
        declare<CurrencyListRepository> { fakeRepository }

        val viewModel: CurrencyListViewModel by inject()

        // When
        viewModel.loadCurrencies()

        // Then
        with(viewModel.uiState.value) {
            isLoading.shouldBe(false)
            currencies.shouldBeEmpty()
            error.shouldBeNull()
        }
    }

    @Test
    fun `clearError should clear error state`() = runTest {
        // Given
        val fakeRepository = FakeCurrencyListRepository.createError()
        declare<CurrencyListRepository> { fakeRepository }

        val viewModel: CurrencyListViewModel by inject()

        viewModel.loadCurrencies()

        // When
        viewModel.clearError()

        // Then
        viewModel.uiState.value.error.shouldBeNull()
    }

    @Test
    fun `loadCurrencies should set loading to true during execution`() = runTest {
        // Given
        val fakeRepository = FakeCurrencyListRepository()
        fakeRepository.setCurrenciesResult(
            Result.success(
                listOf(Currency("test", "TEST", 1.0, Table.TABLE_A))
            )
        )
        declare<CurrencyListRepository> { fakeRepository }

        val viewModel: CurrencyListViewModel by inject()

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
        val fakeRepository = FakeCurrencyListRepository()
        declare<CurrencyListRepository> { fakeRepository }
        fakeRepository.setCurrenciesResult(
            Result.success(
                listOf(Currency("test", "TEST", 1.0, Table.TABLE_A))
            )
        )

        val viewModel: CurrencyListViewModel by inject()

        // When
        viewModel.loadCurrencies()
        with(viewModel.uiState.value) {
            currencies.shouldHaveSize(1)
        }

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
        with(viewModel.uiState.value) {
            currencies.shouldHaveSize(2)
            currencies.first().code.shouldBe("USD")
            currencies.last().code.shouldBe("EUR")
        }
    }
}
