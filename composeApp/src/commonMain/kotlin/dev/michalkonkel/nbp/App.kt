package dev.michalkonkel.nbp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.michalkonkel.nbp.currency_details.presentation.CurrencyDetailsScreen
import dev.michalkonkel.nbp.currency_list.presentation.CurrencyListScreen
import dev.michalkonkel.nbp.navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.CurrencyList,
        ) {
            composable<Screen.CurrencyList> {
                CurrencyListScreen(
                    onCurrencyClick = { currencyCode, table ->
                        navController.navigate(Screen.CurrencyDetails(currencyCode, table))
                    },
                )
            }

            composable<Screen.CurrencyDetails> { backStackEntry ->
                val args = backStackEntry.toRoute<Screen.CurrencyDetails>()
                CurrencyDetailsScreen(
                    currencyCode = args.currencyCode,
                    table = args.table,
                )
            }
        }
    }
}
