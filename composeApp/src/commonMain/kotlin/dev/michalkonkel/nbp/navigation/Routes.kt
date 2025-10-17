package dev.michalkonkel.nbp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object CurrencyList : Screen
    
    @Serializable
    data class CurrencyDetails(val currencyCode: String) : Screen
}