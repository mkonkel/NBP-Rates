package dev.michalkonkel.nbp.currency_list.network.di

import dev.michalkonkel.nbp.currency_list.network.api.CurrencyListApi
import dev.michalkonkel.nbp.currency_list.network.internal.CurrencyListApiImpl
import org.koin.dsl.module

val currencyListNetworkModule =
    module {
        single<CurrencyListApi> { CurrencyListApiImpl(get()) }
    }
