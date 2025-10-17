package dev.michalkonkel.nbp.currency_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.usecase.LoadCurrencyDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * UI state for currency details screen following UDF pattern.
 */
data class CurrencyDetailsUiState(
    val currencyDetails: CurrencyDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * ViewModel for managing currency details screen state.
 * Implements MVVM with Unidirectional Data Flow.
 *
 * @param loadCurrencyDetailsUseCase Use case for loading currency details with highlighting
 */
internal class CurrencyDetailsViewModel(
    private val loadCurrencyDetailsUseCase: LoadCurrencyDetailsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyDetailsUiState())
    val uiState: StateFlow<CurrencyDetailsUiState> = _uiState.asStateFlow()

    /**
     * Loads currency details for specified code.
     */
    fun loadCurrencyDetails(
        code: String,
        table: Table = Table.TABLE_A,
        days: Int = 30,
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            loadCurrencyDetailsUseCase(code, table, days)
                .onSuccess { details ->
                    _uiState.value =
                        _uiState.value.copy(
                            currencyDetails = details,
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
