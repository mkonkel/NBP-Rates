package dev.michalkonkel.nbp.currency_details.data.mapper

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.network.models.CurrencyRateDto
import dev.michalkonkel.nbp.currency_details.network.models.RateDto
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class CurrencyDetailsMapperTest {

    @Test
    fun `mapToDomain should map CurrencyRateDto to CurrencyDetails with complete data`() {
        // Given
        val dto = CurrencyRateDto(
            table = "A",
            currency = "dolar amerykański",
            code = "USD",
            rates = listOf(
                RateDto(
                    no = "1/A/NBP/2024",
                    effectiveDate = "2024-01-02",
                    mid = 4.1234,
                ),
                RateDto(
                    no = "2/A/NBP/2024",
                    effectiveDate = "2024-01-03",
                    mid = 4.2345,
                ),
            ),
        )

        // When
        val result = CurrencyDetailsMapper.mapToDomain(dto)

        // Then
        assertSoftly {
            result.name shouldBe "dolar amerykański"
            result.code shouldBe "USD"
            result.currentRate shouldBe 4.1234
            result.table shouldBe Table.TABLE_A
            result.effectiveDate shouldBe "2024-01-02"
            result.historicalRates.size shouldBe 2

            result.historicalRates[0].effectiveDate shouldBe "2024-01-02"
            result.historicalRates[0].rate shouldBe 4.1234

            result.historicalRates[1].effectiveDate shouldBe "2024-01-03"
            result.historicalRates[1].rate shouldBe 4.2345
        }
    }

    @Test
    fun `mapToDomain should handle table B correctly`() {
        // Given
        val dto = CurrencyRateDto(
            table = "B",
            currency = "dolar australijski",
            code = "AUD",
            rates = listOf(
                RateDto(
                    no = "1/B/NBP/2024",
                    effectiveDate = "2024-01-02",
                    mid = 2.7654,
                ),
            ),
        )

        // When
        val result = CurrencyDetailsMapper.mapToDomain(dto)

        // Then
        result.table shouldBe Table.TABLE_B
    }

    @Test
    fun `mapToDomain should handle invalid table type gracefully`() {
        // Given
        // Invalid table type
      val dto = CurrencyRateDto(
            table = "X",
            currency = "test currency",
            code = "TEST",
            rates = listOf(
                RateDto(
                    no = "1/X/NBP/2024",
                    effectiveDate = "2024-01-02",
                    mid = 1.0,
                ),
            ),
        )

        shouldThrow<IllegalStateException> {
            CurrencyDetailsMapper.mapToDomain(dto)
        }.message shouldBe "Unknown table type: X"
    }

    @Test
    fun `mapToDomain should handle empty rates list`() {
        // Given
        val dto = CurrencyRateDto(
            table = "A",
            currency = "test currency",
            code = "TEST",
            rates = emptyList(),
        )

        // When
        val result = CurrencyDetailsMapper.mapToDomain(dto)

        // Then
        assertSoftly {
            result.currentRate shouldBe 0.0
            result.effectiveDate shouldBe ""
            result.historicalRates shouldBe emptyList()
        }
    }

    @Test
    fun `mapToDomain should handle null values correctly`() {
        // Given
        val dto = CurrencyRateDto(
            table = "C",
            currency = "euro",
            code = "EUR",
            rates = listOf(
                RateDto(
                    no = "1/C/NBP/2024",
                    effectiveDate = "2024-01-02",
                    mid = 4.5678,
                ),
            ),
        )

        // When
        val result = CurrencyDetailsMapper.mapToDomain(dto)

        // Then
        assertSoftly {
            result.table shouldBe Table.TABLE_C
            result.historicalRates.size shouldBe 1
            result.historicalRates[0].effectiveDate shouldBe "2024-01-02"
            result.historicalRates[0].rate shouldBe 4.5678
        }
    }
}
