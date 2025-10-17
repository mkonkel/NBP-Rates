package dev.michalkonkel.nbp.currency_list.network

import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi
import dev.michalkonkel.nbp.currency_list.network.di.currencyListNetworkModule
import dev.michalkonkel.nbp.network.di.coreNetworkModule
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class CurrencyListApiIntegrationTest : KoinTest {

    private val api: CurrencyListApi by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(coreNetworkModule, currencyListNetworkModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `getCurrentRates should make real API call and return serialized data`() = runTest {
        // Given - API instance injected by Koin

        // When - Test JSON serialization with shouldNotThrowAny
        // This tests if the API call and JSON serialization works without throwing serialization exceptions
        shouldNotThrowAny {
            val result = api.getCurrentRates()

            // Then - Verify that JSON serialization worked and we got data
            result.shouldNotBeEmpty()

            val firstCurrency = result.first()
            firstCurrency.currency.shouldBe("dolar amerykański")
            firstCurrency.code.shouldBe("USD")
            firstCurrency.mid.shouldNotBeNull()
            firstCurrency.mid.shouldBeGreaterThan(0.0)
        }
    }

    @Test
    fun `getCurrentRates should properly serialize all currency fields`() = runTest {
        // Given - API instance injected by Koin

        // When - Test JSON serialization with shouldNotThrowAny
        shouldNotThrowAny {
            val result = api.getCurrentRates()

            // Then - Verify that all currency fields are properly serialized
            result.shouldNotBeEmpty()

            result.forEach { currency ->
                currency.currency.shouldNotBeNull()
                currency.currency.shouldNotBeBlank()
                currency.code.shouldNotBeNull()
                currency.code.length.shouldBe(3)
                currency.mid.shouldNotBeNull()
                currency.mid.shouldBeGreaterThan(0.0)
            }
        }
    }
}
