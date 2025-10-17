package dev.michalkonkel.nbp.currency_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.michalkonkel.nbp.currency_list.domain.CurrencyListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for currency list screen following UDF pattern.
 */
data class CurrencyListUiState(
    val currencies: List<dev.michalkonkel.nbp.currency_list.domain.Currency> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * ViewModel for managing currency list screen state.
 * Implements MVVM with Unidirectional Data Flow.
 *
 * @param repository Repository for fetching currency data
 */
class CurrencyListViewModel(
    private val repository: CurrencyListRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyListUiState())
    val uiState: StateFlow<CurrencyListUiState> = _uiState.asStateFlow()

    init {
        loadCurrencies()
    }

    /**
     * Loads currencies from repository and updates UI state.
     */
    fun loadCurrencies() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            repository.getCurrencies()
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
