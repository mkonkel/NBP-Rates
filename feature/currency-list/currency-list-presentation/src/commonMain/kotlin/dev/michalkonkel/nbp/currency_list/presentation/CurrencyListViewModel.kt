package dev.michalkonkel.nbp.currency_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.michalkonkel.nbp.currency_list.domain.Currency
import dev.michalkonkel.nbp.currency_list.presentation.usecase.LoadCurrencyRatesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for currency list screen following UDF pattern.
 */
data class CurrencyListUiState(
    val currencies: List<Currency> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * ViewModel for managing currency list screen state.
 * Implements MVVM with Unidirectional Data Flow.
 *
 * @param loadCurrencyRatesUseCase Use case for loading currency rates from tables A and B in parallel
 */
internal class CurrencyListViewModel(
    private val loadCurrencyRatesUseCase: LoadCurrencyRatesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyListUiState())
    val uiState: StateFlow<CurrencyListUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    /**
     * Loads currencies using use case that makes parallel API calls to tables A and B.
     */
    fun loadCurrencies() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            loadCurrencyRatesUseCase()
                .onSuccess { currencies ->
                    _uiState.value =
                        _uiState.value.copy(
                            currencies = currencies,
                            isLoading = false,
                            error = null,
                        )
                }
                .onFailure { error ->
                    _uiState.value =
                        _uiState.value.copy(
                            isLoading = false,
                            error = error.message,
                        )
                }
        }
    }

    /**
     * Clears error state.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
