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

#### `feature:currency_list` ✅ **COMPLETED**
Displays list of available currencies with current rates:
- **`currency-list-domain`** - Domain models (`Currency`, `CurrencyList`), repository interfaces
- **`currency-list-network`** - NBP API integration with public `CurrencyListApi` interface + internal implementation
- **`currency-list-data`** - Repository implementation with proper DI and error handling
- **`currency-list-presentation`** - ViewModel with UDF state management and public `CurrencyListScreen`
- **`currency-list-di`** - Centralized DI modules aggregating all layers

#### `feature:currency_details` ✅ **COMPLETED**
Shows detailed information and historical rates for selected currency:
- **`currency-details-domain`** - Domain models (`CurrencyDetails`, `HistoricalRate`), repository interfaces
- **`currency-details-network`** - Historical rate API with public `CurrencyDetailsApi` interface + internal implementation
- **`currency-details-data`** - Repository implementation with proper DI and error handling
- **`currency-details-presentation`** - ViewModel with UDF state management and public `CurrencyDetailsScreen`
- **`currency-details-di`** - Centralized DI modules aggregating all layers

### Application Module
- **`composeApp`** - Main application entry point, platform-specific implementations, and dependency injection setup

## Implementation Status ✅

Both feature modules are **fully implemented** with:
- ✅ Complete layered architecture (API → Domain → Data → Presentation)
- ✅ Proper visibility modifiers (public interfaces, internal implementations)
- ✅ Dependency injection with Koin
- ✅ Android-first KMP configuration
- ✅ Code quality with Detekt and Spotless
- ✅ Comprehensive unit tests with fake implementations
- ✅ Test isolation and clean code practices
- ✅ Buildable modules with proper dependencies

## Technology Stack

- **Kotlin Multiplatform** - Cross-platform development
- **Jetpack Compose** - Declarative UI framework
- **Ktor** - HTTP client for API communication
- **Kotlinx.serialization** - JSON parsing
- **Koin** - Dependency injection
- **Kotest** - Modern testing framework with expressive assertions
- **Detekt & Spotless** - Code quality and formatting

## Key Features

### Currency List (`currency_list`)
- Display current exchange rates from NBP Table A
- Real-time data fetching with loading states
- Error handling and retry mechanisms
- Clean MVVM architecture with UDF

### Currency Details (`currency_details`)
- Detailed currency information with current rate
- Historical rates (configurable days, default 30)
- Interactive charts and rate trends
- Currency-specific data visualization

## NBP API Integration

The application consumes the official NBP API endpoints:
- **Current rates**: `/api/exchangerates/tables/a` (Table A)
- **Historical rates**: `/api/exchangerates/rates/a/{code}/last/{days}`

## Development Setup

### Prerequisites
- JDK 17+
- Android Studio or IntelliJ IDEA
- Kotlin Multiplatform plugin

### Build Commands

#### Android Application
```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Run on connected device/emulator
./gradlew :composeApp:installDebug
```

#### Code Quality
```bash
# Run code formatting
./gradlew spotlessApply

# Run static analysis
./gradlew detekt
```

#### Testing
```bash
# Run all unit tests
./gradlew test

# Run specific feature tests
./gradlew :feature:currency-list:currency-list-presentation:test
./gradlew :feature:currency-details:currency-details-presentation:test
```

#### Module-specific builds
```bash
# Build individual feature modules
./gradlew :feature:currency-list:build
./gradlew :feature:currency-details:build
```

## Project Structure

```
feature/
├── currency_list/
│   ├── currency-list-domain/     # Domain models + repository interfaces
│   ├── currency-list-network/    # API service + DTOs (public/internal)
│   ├── currency-list-data/        # Repository implementations
│   ├── currency-list-presentation/ # ViewModels + Compose UI
│   └── currency-list-di/          # Centralized DI modules
└── currency_details/
    ├── currency-details-domain/
    ├── currency-details-network/
    ├── currency-details-data/
    ├── currency-details-presentation/
    └── currency-details-di/
```

## Quality Standards

- **Detekt**: Zero critical issues enforced
- **Spotless**: Consistent formatting with ktlint
- **Architecture**: Clean separation of concerns
- **Testing**: Repository pattern with Result<T> for error handling
- **Test Coverage**: Unit tests with fake implementations and proper isolation
- **Code Quality**: Use of `with()` blocks to eliminate code duplication
- **Null Safety**: Proper null handling with `shouldNotBeNull()` instead of `!!`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)