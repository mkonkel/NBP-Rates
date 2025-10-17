package dev.michalkonkel.nbp.currency_list.domain

import dev.michalkonkel.nbp.core.domain.Table

data class CurrencyTable(
    val table: Table,
    val no: String,
    val effectiveDate: String,
    val rates: List<Currency>,
)

/**
 * Currency entity for display purposes.
 */
data class Currency(
    val name: String,
    val code: String,
    val currentRate: Double,
    val table: Table,
)
