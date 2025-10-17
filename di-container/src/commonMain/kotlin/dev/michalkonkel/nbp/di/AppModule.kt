package dev.michalkonkel.nbp.di

import dev.michalkonkel.nbp.currency_details.di.currencyDetailsModules
import dev.michalkonkel.nbp.currency_list.di.currencyListModules
import dev.michalkonkel.nbp.network.di.coreNetworkModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

private val appModule =
    module {
        includes(
            coreNetworkModule,
        )
    }

private val featureModules =
    buildList {
        addAll(currencyListModules)
        addAll(currencyDetailsModules)
    }

fun startDI() {
    val allModules = appModule + featureModules
    startKoin {
        modules(allModules)
    }
}
