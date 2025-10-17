# NBP Rates

Kotlin Multiplatform mobile application displaying currency exchange rates from the National Bank of Poland (NBP) API.

## Application Overview

The app provides real-time and historical exchange rate information with support for Android and iOS platforms using Jetpack Compose for UI.

## Architecture

The project follows **feature-specific modular architecture** with MVVM pattern and Unidirectional Data Flow (UDF):

### Core Modules
- **`core:network`** - Shared HTTP client factory using Ktor for API communication
- **`core:ui`** - Shared UI components and Compose utilities
- **`core:database`** - Shared database layer (Room/SQLite)

### Feature Modules

#### `feature:currency_list`
Displays list of available currencies with current rates:
- **`currency-list-domain`** - Domain models, repository interfaces, business logic
- **`currency-list-network`** - NBP API integration and data transfer objects  
- **`currency-list-data`** - Repository implementations connecting network to domain
- **`currency-list-presentation`** - ViewModels and Compose UI screens

#### `feature:currency_details` 
Shows detailed information and historical rates for selected currency:
- **`currency-details-domain`** - Domain models and repository interfaces
- **`currency-details-network`** - Historical rate API integration
- **`currency-details-data`** - Repository implementations
- **`currency-details-presentation`** - ViewModels and detail screens

### Application Module
- **`composeApp`** - Main application entry point, platform-specific implementations, and dependency injection setup

## Technology Stack

- **Kotlin Multiplatform** - Cross-platform development
- **Jetpack Compose** - Declarative UI framework
- **Ktor** - HTTP client for API communication
- **Kotlinx.serialization** - JSON parsing
- **Koin** - Dependency injection
- **Detekt & Spotless** - Code quality and formatting

## NBP API Integration

The application consumes the official NBP API endpoints:
- Current exchange rates (`/api/exchangerates/tables/{table}`)
- Historical rates and currency details

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…