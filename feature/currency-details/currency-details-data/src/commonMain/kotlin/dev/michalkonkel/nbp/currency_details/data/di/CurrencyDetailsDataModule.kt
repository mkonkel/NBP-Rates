package dev.michalkonkel.nbp.currency_details.data.di

import dev.michalkonkel.nbp.currency_details.data.CurrencyDetailsRepositoryImpl
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetailsRepository
import org.koin.dsl.module

val currencyDetailsDataModule =
    module {
        single<CurrencyDetailsRepository> {
            CurrencyDetailsRepositoryImpl(get())
        }
    }
