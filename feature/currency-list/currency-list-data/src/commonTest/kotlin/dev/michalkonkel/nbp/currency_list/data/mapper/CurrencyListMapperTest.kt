package dev.michalkonkel.nbp.currency_list.data.mapper

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.network.models.CurrencyDto
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.shouldBe
import org.junit.Test

class CurrencyListMapperTest {
    
    @Test
    fun `mapToDomain should map single CurrencyDto to Currency`() {
        // Given
        val dto = CurrencyDto(
            currency = "dolar amerykański",
            code = "USD",
            mid = 4.1234,
        )
        
        // When
        val result = CurrencyListMapper.mapToDomain(dto)
        
        // Then
        assertSoftly {
            result.name shouldBe "dolar amerykański"
            result.code shouldBe "USD"
            result.currentRate shouldBe 4.1234
            result.table shouldBe Table.TABLE_A
        }
    }
    
    @Test
    fun `mapToDomain should handle null mid value`() {
        // Given
        val dto = CurrencyDto(
            currency = "euro",
            code = "EUR",
            mid = null,
        )
        
        // When
        val result = CurrencyListMapper.mapToDomain(dto)
        
        // Then
        result.currentRate shouldBe 0.0
    }
    
    @Test
    fun `mapToDomain should map list of CurrencyDto to list of Currency`() {
        // Given
        val dtos = listOf(
            CurrencyDto(
                currency = "dolar amerykański",
                code = "USD",
                mid = 4.1234,
            ),
            CurrencyDto(
                currency = "euro",
                code = "EUR",
                mid = 4.5678,
            ),
            CurrencyDto(
                currency = "funt szterling",
                code = "GBP",
                mid = 5.2345,
            ),
        )
        
        // When
        val result = CurrencyListMapper.mapToDomain(dtos)
        
        // Then
        assertSoftly {
            result.size shouldBe 3
            result[0].code shouldBe "USD"
            result[0].name shouldBe "dolar amerykański"
            result[0].currentRate shouldBe 4.1234
            result[0].table shouldBe Table.TABLE_A
            
            result[1].code shouldBe "EUR"
            result[1].name shouldBe "euro"
            result[1].currentRate shouldBe 4.5678
            result[1].table shouldBe Table.TABLE_A
            
            result[2].code shouldBe "GBP"
            result[2].name shouldBe "funt szterling"
            result[2].currentRate shouldBe 5.2345
            result[2].table shouldBe Table.TABLE_A
        }
    }
    
    @Test
    fun `mapToDomain should handle empty list`() {
        // Given
        val dtos = emptyList<CurrencyDto>()
        
        // When
        val result = CurrencyListMapper.mapToDomain(dtos)
        
        // Then
        result shouldBe emptyList()
    }
}