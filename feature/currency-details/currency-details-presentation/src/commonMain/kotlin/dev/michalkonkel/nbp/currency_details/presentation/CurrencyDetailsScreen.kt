package dev.michalkonkel.nbp.currency_details.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.michalkonkel.nbp.core.domain.Table
import dev.michalkonkel.nbp.currency_details.domain.CurrencyDetails
import dev.michalkonkel.nbp.currency_details.domain.HistoricalRate
import org.koin.compose.koinInject

@Composable
fun CurrencyDetailsScreen(
    currencyCode: String,
    table: Table,
    modifier: Modifier = Modifier,
) {
    val viewModel: CurrencyDetailsViewModel = koinInject()
    val state by viewModel.uiState.collectAsState()

    // Load currency details when screen is composed
    androidx.compose.runtime.LaunchedEffect(currencyCode, table) {
        viewModel.loadCurrencyDetails(currencyCode, table)
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .padding(16.dp),
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error,
                )
            }

            state.currencyDetails != null -> {
                LazyColumn {
                    item {
                        CurrencyInfoCard(currencyDetails = state.currencyDetails!!)
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Historical Rates",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    items(state.currencyDetails!!.historicalRates) { rate ->
                        HistoricalRateItem(rate = rate)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyInfoCard(currencyDetails: CurrencyDetails) {
    Card(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = currencyDetails.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currencyDetails.code,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Current Rate: ${currencyDetails.currentRate}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Effective Date: ${currencyDetails.effectiveDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Table: ${currencyDetails.table}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HistoricalRateItem(rate: HistoricalRate) {
    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
            val color = if (rate.isHighlighted) {
                Color.Red
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }

            Text(
                text = rate.effectiveDate,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = color
            )
            Text(
                text = "Rate: ${rate.rate}",
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}
