package dev.michalkonkel.nbp.currency_details.network.di

import dev.michalkonkel.nbp.currency_details.network.api.CurrencyDetailsApi
import dev.michalkonkel.nbp.currency_details.network.internal.CurrencyDetailsApiImpl
import org.koin.dsl.module

val currencyDetailsNetworkModule =
    module {
        single<CurrencyDetailsApi> { CurrencyDetailsApiImpl(get()) }
    }
