package dev.michalkonkel.nbp.currency_list.network.di

import dev.michalkonkel.nbp.core.network.HttpClientFactory
import dev.michalkonkel.nbp.currency_list.network.CurrencyListNetworkService
import org.koin.dsl.module

val currencyListNetworkModule = module {
    single {
        CurrencyListNetworkService(get())
    }
}