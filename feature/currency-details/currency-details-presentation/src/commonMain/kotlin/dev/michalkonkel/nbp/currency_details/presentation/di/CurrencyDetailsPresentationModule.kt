package dev.michalkonkel.nbp.currency_details.presentation.di

import dev.michalkonkel.nbp.currency_details.presentation.CurrencyDetailsViewModel
import dev.michalkonkel.nbp.currency_details.usecase.LoadCurrencyDetailsUseCase
import dev.michalkonkel.nbp.currency_details.usecase.LoadCurrencyDetailsUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val currencyDetailsPresentationModule =
    module {
        viewModel {
            CurrencyDetailsViewModel(get())
        }

        factory<LoadCurrencyDetailsUseCase> {
            LoadCurrencyDetailsUseCaseImpl(get())
        }
    }
