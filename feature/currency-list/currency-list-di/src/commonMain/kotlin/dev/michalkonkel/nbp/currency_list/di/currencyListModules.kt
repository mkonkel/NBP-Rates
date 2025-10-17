package dev.michalkonkel.nbp.currency_list.di

import dev.michalkonkel.nbp.currency_list.data.di.currencyListDataModule
import dev.michalkonkel.nbp.currency_list.network.di.currencyListNetworkModule
import dev.michalkonkel.nbp.currency_list.presentation.di.currencyListPresentationModule

// shoudl be added to the mian module
val currencyListModules =
    buildList {
        add(currencyListNetworkModule)
        add(currencyListDataModule)
        add(currencyListPresentationModule)
    }
