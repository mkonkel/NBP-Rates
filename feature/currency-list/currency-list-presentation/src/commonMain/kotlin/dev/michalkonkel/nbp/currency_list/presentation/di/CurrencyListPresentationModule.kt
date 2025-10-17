package dev.michalkonkel.nbp.currency_list.presentation.di

import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListViewModel
import dev.michalkonkel.nbp.currency_list.presentation.usecase.LoadCurrencyRatesUseCase
import dev.michalkonkel.nbp.currency_list.presentation.usecase.LoadCurrencyRatesUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val currencyListPresentationModule =
    module {
        factory<LoadCurrencyRatesUseCase> { LoadCurrencyRatesUseCaseImpl(get()) }

        viewModel {
            CurrencyListViewModel(get())
        }
    }
