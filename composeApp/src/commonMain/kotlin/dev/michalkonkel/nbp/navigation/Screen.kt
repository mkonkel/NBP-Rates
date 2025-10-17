package dev.michalkonkel.nbp.navigation

import dev.michalkonkel.nbp.core.domain.Table
import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object CurrencyList : Screen

    @Serializable
    data class CurrencyDetails(
        val currencyCode: String,
        val table: Table,
    ) : Screen
}
