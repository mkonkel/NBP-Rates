package dev.michalkonkel.nbp.currency_list.data.mapper

import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.network.models.CurrencyDto

/**
 * Mapper for converting currency DTOs to domain models.
 */
internal object CurrencyListMapper {
    /**
     * Maps a single CurrencyDto to Currency domain model.
     */
    fun mapToDomain(dto: CurrencyDto): Currency =
        Currency(
            name = dto.currency,
            code = dto.code,
            currentRate = dto.mid ?: 0.0,
            table = Table.TABLE_A,
        )

    /**
     * Maps a list of CurrencyDto to list of Currency domain models.
     */
    fun mapToDomain(dtos: List<CurrencyDto>): List<Currency> = dtos.map { mapToDomain(it) }
}
