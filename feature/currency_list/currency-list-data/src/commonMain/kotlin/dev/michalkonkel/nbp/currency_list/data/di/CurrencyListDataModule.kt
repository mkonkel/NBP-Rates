package dev.michalkonkel.nbp.currency_list.data.di

import dev.michalkonkel.nbp.currency_list.data.CurrencyListRepositoryImpl
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.network.CurrencyListNetworkService
import org.koin.dsl.module

val currencyListDataModule = module {
    single<CurrencyListRepository> {
        CurrencyListRepositoryImpl(get())
    }
}