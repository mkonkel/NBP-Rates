package dev.michalkonkel.nbp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        CurrencyListScreen()
    }
}
