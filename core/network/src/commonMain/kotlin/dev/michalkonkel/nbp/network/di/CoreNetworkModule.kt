package dev.michalkonkel.nbp.network.di

import dev.michalkonkel.nbp.network.HttpClientFactory
import org.koin.dsl.module

val coreNetworkModule =
    module {
        single { HttpClientFactory }
    }
