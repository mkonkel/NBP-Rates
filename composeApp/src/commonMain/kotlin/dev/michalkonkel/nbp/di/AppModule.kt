package dev.michalkonkel.nbp.di

import dev.michalkonkel.nbp.core.network.di.coreNetworkModule
import dev.michalkonkel.nbp.currency_list.di.currencyListModule
import org.koin.dsl.module

val appModule = module {
    includes(
        coreNetworkModule,
        currencyListModule
    )
}