package dev.michalkonkel.nbp.currency_list.data

import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import dev.michalkonkel.nbp.currency_list.network.CurrencyListNetworkService

class CurrencyListRepositoryImpl(
    private val networkService: CurrencyListNetworkService,
) : CurrencyListRepository {
    override suspend fun getCurrencies(): Result<List<Currency>> {
        return networkService.getCurrentRates()
            .map { dtos ->
                dtos.map { dto ->
                    Currency(
                        name = dto.currency,
                        code = dto.code,
                        currentRate = dto.mid ?: 0.0,
                        table = "A",
                    )
                }
            }
    }
}
