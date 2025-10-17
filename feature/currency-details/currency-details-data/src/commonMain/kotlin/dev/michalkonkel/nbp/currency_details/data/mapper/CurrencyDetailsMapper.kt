package dev.michalkonkel.nbp.currency_details.data.mapper

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.HistoricalRate
import dev.michalkonkel.nbp.currency_details.network.models.CurrencyRateDto
import dev.michalkonkel.nbp.currency_details.network.models.RateDto

/**
 * Mapper for converting currency rate DTOs to domain models.
 */
internal object CurrencyDetailsMapper {
    /**
     * Maps a single RateDto to HistoricalRate domain model.
     */
    private fun mapRateToDomain(rate: RateDto): HistoricalRate =
        HistoricalRate(
            effectiveDate = rate.effectiveDate,
            rate = rate.mid,
        )

    /**
     * Maps CurrencyRateDto to CurrencyDetails domain model.
     */
    fun mapToDomain(dto: CurrencyRateDto): CurrencyDetails {
        val firstRate = dto.rates.firstOrNull()

        return CurrencyDetails(
            name = dto.currency,
            code = dto.code,
            currentRate = firstRate?.mid ?: 0.0,
            table = Table.fromString(dto.table),
            effectiveDate = firstRate?.effectiveDate ?: "",
            historicalRates = dto.rates.map { mapRateToDomain(it) },
        )
    }
}
