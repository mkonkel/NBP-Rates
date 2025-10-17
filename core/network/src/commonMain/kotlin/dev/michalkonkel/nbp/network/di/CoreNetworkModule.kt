package dev.michalkonkel.nbp.network.di

import dev.michalkonkel.nbp.network.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.dsl.module

val coreNetworkModule =
    module {
        single<HttpClient> { HttpClientFactory().create() }
    }
