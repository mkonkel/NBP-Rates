# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Claude Configuration

This project uses `.claude/settings.local.json` for Claude-specific settings. Key configurations:
- `includeCoAuthoredBy: false` - Do not add "Co-Authored-By" lines to commit messages
- Follow all project-specific conventions and architectural rules mentioned below

## Project Overview

NBP Rates is a Kotlin Multiplatform mobile application for displaying exchange rates from the National Bank of Poland (NBP) API. The app targets Android and iOS platforms using Jetpack Compose for UI.

**Current Status: ✅ FULLY IMPLEMENTED** - Both currency list and details features are complete with production-ready architecture.

## Architecture

This project follows **feature-specific modular architecture** with MVVM pattern and Unidirectional Data Flow (UDF):

### Feature-Specific Architecture Pattern
Each feature has its own complete set of layers:
```
feature/{feature-name}/
├── {feature-name}-domain/       # Domain models + repository interfaces
├── {feature-name}-network/      # API service + DTOs (public/internal)
├── {feature-name}-data/          # Repository implementations
├── {feature-name}-presentation/ # ViewModels + Compose UI
└── {feature-name}-di/           # Centralized DI modules
```

### Module Structure

#### Core Modules
- **`:core:network`** - HTTP client factory using Ktor, shared across features
- **`:core:ui`** - Shared UI components and utilities
- **`:core:database`** - Database layer (Room/SQLite)

#### Feature Modules

##### `feature:currency_list` ✅ COMPLETED
- **Purpose**: Display list of available currencies with current rates
- **Submodules**: domain, network, data, presentation, di
- **Key Components**: `CurrencyListScreen`, `CurrencyListViewModel`, `CurrencyListApi`

##### `feature:currency_details` ✅ COMPLETED  
- **Purpose**: Show detailed currency information with historical rates
- **Submodules**: domain, network, data, presentation, di
- **Key Components**: `CurrencyDetailsScreen`, `CurrencyDetailsViewModel`, `CurrencyDetailsApi`

#### Application Module
- **`:composeApp`** - Main application entry point with DI configuration

### Key Architectural Rules
- **Public interfaces, internal implementations** - Clean API boundaries
- **MVVM with UDF pattern** - Strict separation of concerns
- **Repository pattern with Result<T>** - Proper error handling
- **Dependency Injection with Koin** - Testable and maintainable
- **Android-first KMP** - Current configuration supports Android, iOS prepared

## Development Commands

### Building
```bash
# Build Android app
./gradlew :composeApp:assembleDebug

# Build entire project
./gradlew build

# Build specific feature modules
./gradlew :feature:currency-list:build
./gradlew :feature:currency-details:build
```

### Code Quality
```bash
# Run static analysis
./gradlew detekt

# Auto-format code with Spotless
./gradlew spotlessApply

# Run all quality checks
./gradlew check
```

### Testing & Module Builds
```bash
# Build specific layer
./gradlew :feature:currency-list:currency-list-presentation:build
./gradlew :feature:currency-details:currency-details-domain:build

# Run all tests
./gradlew test

# Run specific feature tests
./gradlew :feature:currency-list:currency-list-presentation:test
./gradlew :feature:currency-details:currency-details-presentation:test
```

## Technology Stack

- **Kotlin Multiplatform** - Cross-platform development
- **Jetpack Compose** - Declarative UI framework
- **Ktor** - HTTP client for network operations
- **Kotlinx.serialization** - JSON serialization
- **Koin** - Dependency injection framework
- **Kotest** - Modern testing framework with expressive assertions
- **Detekt** - Static code analysis (zero critical issues)
- **Spotless** - Code formatting with ktlint
- **Android Target SDK**: 36, **Min SDK**: 24
- **iOS**: arm64 and simulator arm64 targets (prepared)

## NBP API Integration

### Current Endpoints
- **Current rates**: `/api/exchangerates/tables/a` (Table A)
- **Historical rates**: `/api/exchangerates/rates/a/{code}/last/{days}`

### API Architecture
- **Public API interfaces** in each feature's network layer
- **Internal implementations** hidden from other layers
- **DTO-to-Domain mapping** in repository implementations
- **Result<T> pattern** for proper error handling

## Adding New Features

Follow the established feature-specific architecture pattern:

### 1. Create Module Structure
```bash
mkdir -p feature/{new-feature}/{new-feature}-{domain,network,data,presentation,di}/src/commonMain/kotlin/dev/michalkonkel/nbp/{new_feature}
```

### 2. Implement Layers
- **Domain**: Models + repository interfaces (public)
- **Network**: API interface (public) + internal implementation + DTOs
- **Data**: Repository implementations (internal)
- **Presentation**: ViewModels (internal) + public screens + UseCase interfaces (public) + UseCase implementations (internal)
- **DI**: Centralized modules aggregating all layers

### 3. UseCase Implementation Rules
- **Interface**: Define in presentation layer with public visibility
- **Implementation**: Create in presentation layer with internal visibility  
- **TODO Comments**: Mark UseCase files with TODO comments about missing tests and future layer migration
- **Testing**: ALWAYS create comprehensive unit tests for all UseCase implementations
- **Migration Plan**: Document in TODO that UseCases should move to dedicated use-case layer

### 4. Update Configuration
- Add module to `settings.gradle.kts`
- Include DI modules in main app configuration
- Add necessary dependencies

## Code Standards & Conventions

### Visibility Modifiers
- **Public**: Interfaces, Screens, API contracts
- **Internal**: Implementations, ViewModels, Services
- **Package-private**: Helper classes within same package

### Repository Pattern
```kotlin
// Always use Result<T> for error handling
suspend fun getCurrencies(): Result<List<Currency>>

// Public interface
interface CurrencyListRepository

// Internal implementation
internal class CurrencyListRepositoryImpl(...)
```

### Testing Pattern
```kotlin
// Use fake implementations for testing
class FakeCurrencyListRepository : CurrencyListRepository {
    // Static test data for deterministic tests
    override suspend fun getCurrencies(): Result<List<Currency>>
}

// Test isolation with fresh instances per test
@Test
fun `test name`() = runTest {
    val fakeRepository = FakeCurrencyListRepository()
    declare<CurrencyListRepository> { fakeRepository }
    val viewModel: CurrencyListViewModel by inject()
    
    // Use with() blocks to eliminate code duplication
    with(viewModel.uiState.value) {
        isLoading.shouldBe(false)
        currencies.shouldHaveSize(3)
        error.shouldBeNull()
    }
}
```

### Dependency Injection
```kotlin
// API layer - only interface exposed
single<CurrencyListApi> { CurrencyListApiService(get()) }

// Data layer - repository interface exposed  
single<CurrencyListRepository> { CurrencyListRepositoryImpl(get()) }

// Presentation layer - ViewModel injected
viewModel { CurrencyListViewModel(get()) }
```

### Conventions
- All public APIs must have comprehensive KDoc documentation
- Use conventional commits format (WITHOUT Co-Authored-By lines)
- Run `./gradlew spotlessApply` and `./gradlew detekt` before commits
- Maintain 120 character maximum line length
- Follow feature-specific modular architecture strictly
- Respect `.claude/settings.local.json` configuration
- **Testing**: Use fake implementations, proper test isolation, and `with()` blocks
- **Null Safety**: Use `shouldNotBeNull()` instead of `!!` operators in tests
- **Code Quality**: Eliminate code duplication with proper scoping constructs

## Quality Standards

- **Detekt**: Zero critical issues enforced
- **Spotless**: Consistent formatting with ktlint  
- **Architecture**: Clean separation of concerns with proper layering
- **Build**: All modules build successfully with Android-first configuration
- **DI**: Proper dependency injection with Koin
- **Error Handling**: Repository pattern with Result<T>
- **Test Coverage**: Unit tests with fake implementations for all ViewModels
- **Test Isolation**: Each test uses fresh instances to avoid interference
- **Code Quality**: Use of `with()` blocks and proper null handling patterns

## Current Implementation Status

### ✅ Completed Features
- **Currency List Module**: Full implementation with current rates display and comprehensive unit tests
- **Currency Details Module**: Full implementation with historical rates and comprehensive unit tests  
- **Core Infrastructure**: Network factory, DI setup, quality tools
- **Architecture**: Feature-specific modular pattern established
- **Testing Framework**: Complete unit test suite with fake implementations and proper isolation
- **Code Quality**: Clean code practices with `with()` blocks and null safety patterns

### 🚧 Implementation Notes
- Repository mappings are marked with TODO() for future implementation
- Android-first KMP configuration (iOS support prepared but not functional)
- All modules build successfully and pass quality checks
- Public interfaces properly exposed for app integration

### 🚧 Technical Debt & Known Issues

#### UseCase Architecture
- **Current Location**: UseCase interfaces and implementations are temporarily placed in presentation layer
- **TODO Requirements**: All UseCase files must include TODO comments indicating:
  - Missing unit tests (high priority)
  - Future migration to dedicated use-case layer
- **Testing Requirement**: ALL UseCase implementations must have comprehensive unit tests before being considered complete

#### iOS Platform Support
- **Status**: iOS build is prepared but non-functional
- **Missing**: Koin dependency injection initialization in iOS app entry point
- **Impact**: iOS application will not start without proper DI setup
- **Solution**: Add Koin initialization in iOS main function

