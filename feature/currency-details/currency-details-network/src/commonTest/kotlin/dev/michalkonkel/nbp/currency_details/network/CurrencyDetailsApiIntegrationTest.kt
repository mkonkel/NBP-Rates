package dev.michalkonkel.nbp.currency_details.network

import dev.michalkonkel.nbp.currency_details.network.api.CurrencyDetailsApi
import dev.michalkonkel.nbp.currency_details.network.di.currencyDetailsNetworkModule
import dev.michalkonkel.nbp.network.di.coreNetworkModule
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotBeBlank
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

class CurrencyDetailsApiIntegrationTest : KoinTest {
    private val api: CurrencyDetailsApi by inject()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(coreNetworkModule, currencyDetailsNetworkModule)
        }
    }

    @AfterTest
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `getCurrencyRatesLastDays should make real API call and return serialized data`() =
        runTest {
            // Given - API instance injected by Koin
            val code = "USD"
            val days = 5

            // When - Test JSON serialization with shouldNotThrowAny
            shouldNotThrowAny {
                val result = api.getCurrencyRatesLastDays(code, "A", days)

                // Then - Verify that JSON serialization worked and we got data
                result.table.shouldBe("A")
                result.currency.shouldContain("amerykański")
                result.code.shouldBe("USD")
                result.rates.shouldNotBeEmpty()
                result.rates.size.shouldBe(days)

                val firstRate = result.rates.first()
                firstRate.no.shouldNotBeNull()
                firstRate.effectiveDate.shouldNotBeNull()
                firstRate.mid.shouldBeGreaterThan(0.0)
            }
        }

    @Test
    fun `getCurrencyRatesLastDays should properly serialize all currency fields`() =
        runTest {
            // Given - API instance injected by Koin
            val code = "EUR"
            val days = 3

            // When - Test JSON serialization with shouldNotThrowAny
            shouldNotThrowAny {
                val result = api.getCurrencyRatesLastDays(code, "A", days)

                // Then - Verify that all currency and rate fields are properly serialized
                result.table.shouldBe("A") // Simplified for test
                result.currency.shouldNotBeNull()
                result.currency.shouldNotBeBlank()
                result.code.shouldBe("EUR")
                result.rates.shouldNotBeEmpty()
                result.rates.size.shouldBe(days)

                result.rates.forEach { rate ->
                    rate.no.shouldNotBeNull()
                    rate.no.shouldNotBeBlank()
                    rate.effectiveDate.shouldNotBeNull()
                    rate.effectiveDate.shouldMatch(Regex("\\d{4}-\\d{2}-\\d{2}"))
                    rate.mid.shouldBeGreaterThan(0.0)
                }
            }
        }
}
