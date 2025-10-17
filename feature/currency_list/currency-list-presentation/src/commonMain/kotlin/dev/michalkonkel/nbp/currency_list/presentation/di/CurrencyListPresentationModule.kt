package dev.michalkonkel.nbp.currency_list.presentation.di

import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val currencyListPresentationModule = module {
    viewModel {
        CurrencyListViewModel(get())
    }
}