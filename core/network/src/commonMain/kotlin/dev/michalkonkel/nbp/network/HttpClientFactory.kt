package dev.michalkonkel.nbp.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HttpClient instances.
 * Provides HTTP client for NBP API communication.
 */
internal class HttpClientFactory {
    fun create(): HttpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    },
                )
            }

            defaultRequest {
                url("https://api.nbp.pl/api/")
            }
        }
}
