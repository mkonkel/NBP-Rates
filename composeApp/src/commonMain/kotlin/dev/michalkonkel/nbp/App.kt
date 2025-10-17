package dev.michalkonkel.nbp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListScreen
import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: CurrencyListViewModel = koinInject()
        
        CurrencyListScreen(
            viewModel = viewModel,
            modifier = Modifier.fillMaxSize().padding()
        )
    }
}
