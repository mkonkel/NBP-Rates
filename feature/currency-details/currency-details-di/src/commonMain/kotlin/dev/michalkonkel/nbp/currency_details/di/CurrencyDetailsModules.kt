package dev.michalkonkel.nbp.currency_details.di

import dev.michalkonkel.nbp.currency_details.data.di.currencyDetailsDataModule
import dev.michalkonkel.nbp.currency_details.network.di.currencyDetailsNetworkModule
import dev.michalkonkel.nbp.currency_details.presentation.di.currencyDetailsPresentationModule

// shoudl be added to the mian module
val currencyDetailsModules =
    buildList {
        add(currencyDetailsNetworkModule)
        add(currencyDetailsDataModule)
        add(currencyDetailsPresentationModule)
    }
