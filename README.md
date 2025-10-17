# NBP Rates

Kotlin Multiplatform mobile application displaying currency exchange rates from the National Bank of Poland (NBP) API.

## 📱 Application Overview

The NBP Rates app is a modern, cross-platform mobile application that provides real-time and historical exchange rate information for Polish and international currencies. Built with Kotlin Multiplatform and Jetpack Compose, it offers a clean, intuitive interface for checking current rates and analyzing historical trends.

**Target Platforms:**
- ✅ **Android**: Fully functional and production-ready
- 🚧 **iOS**: Builds successfully but requires Koin DI initialization to run

## 🏗️ Architecture

This project follows **feature-specific modular architecture** with MVVM pattern and Unidirectional Data Flow (UDF):

### 🎯 Architecture Principles
- **Feature-First Design**: Each feature has its own complete module stack
- **Clean Architecture**: Clear separation between layers with proper boundaries
- **MVVM + UDF**: ViewModel manages state, UI observes and reacts to changes
- **Dependency Injection**: Koin provides testable and maintainable DI
- **Public Interfaces, Internal Implementations**: Clean API boundaries

### 📦 Module Structure

```
NBP-Rates/
├── core/                           # Shared infrastructure
│   ├── network/                   # HTTP client factory (Ktor)
│   ├── ui/                        # Shared UI components & utilities
│   └── database/                  # Database layer (Room/SQLite)
├── feature/
│   ├── currency_list/             # Currency listing feature
│   │   ├── currency-list-domain/          # Domain models + repository interfaces
│   │   ├── currency-list-network/         # NBP API integration + DTOs
│   │   ├── currency-list-data/            # Repository implementations
│   │   ├── currency-list-presentation/    # ViewModels + UseCases + UI
│   │   └── currency-list-di/              # DI modules
│   └── currency_details/          # Currency details feature
│       ├── currency-details-domain/        # Domain models + repository interfaces
│       ├── currency-details-network/       # Historical rates API + DTOs
│       ├── currency-details-data/          # Repository implementations
│       ├── currency-details-presentation/  # ViewModels + UseCases + UI
│       └── currency-details-di/            # DI modules
└── composeApp/                    # Main application entry point
```

## 🚀 Features

### 💱 Currency List (`currency_list`)
- **Real-time Exchange Rates**: Fetch current rates from NBP Tables A and B in parallel
- **Comprehensive Currency Coverage**: All major international currencies
- **Loading States**: Visual feedback during data fetching
- **Error Handling**: Graceful error handling with retry mechanisms
- **Clean Architecture**: MVVM with proper state management

### 📊 Currency Details (`currency_details`)
- **Detailed Currency Information**: Comprehensive data for selected currencies
- **Historical Rates**: Configurable historical data (default 30 days)
- **Interactive Charts**: Visual representation of rate trends
- **Dynamic Table Types**: Support for Tables A, B, and C
- **Rate Highlighting**: Visual indicators for significant rate changes (10% threshold)
- **Advanced Filtering**: Date range and table type selection

## 🔧 Technology Stack

### Core Technologies
- **Kotlin Multiplatform**: Cross-platform development with native performance
- **Jetpack Compose**: Modern declarative UI framework
- **Ktor**: HTTP client for API communication with proper error handling
- **Kotlinx.serialization**: Efficient JSON parsing and serialization
- **Koin**: Dependency injection framework for testable architecture

### Testing & Quality
- **Kotest**: Modern testing framework with expressive assertions
- **Turbine**: Specialized library for testing StateFlow and coroutines
- **Detekt**: Static code analysis (zero critical issues enforced)
- **Spotless**: Consistent code formatting with ktlint

### Development Tools
- **Android Target SDK**: 36, **Min SDK**: 24
- **iOS Targets**: arm64 and simulator arm64 (prepared)
- **Gradle**: Build system with Kotlin DSL

## 🔌 NBP API Integration

### Current Endpoints
- **Current Rates**: `/api/exchangerates/tables/a` and `/api/exchangerates/tables/b`
- **Historical Rates**: `/api/exchangerates/rates/a/{code}/last/{days}`

### API Architecture
- **Parallel API Calls**: Optimized performance with concurrent requests
- **Result<T> Pattern**: Comprehensive error handling throughout the stack
- **DTO-to-Domain Mapping**: Clean separation between API and domain models
- **Rate Limiting Awareness**: Built with NBP API constraints in mind

## 🚧 Technical Debt & Known Issues

### 🔄 UseCase Architecture (High Priority)
- **Current State**: UseCase classes temporarily placed in `presentation` layer
- **Required Action**: Move to dedicated `use-case` layer for proper architectural separation
- **Missing Tests**: UseCase implementations lack comprehensive unit tests
- **Impact**: Violates clean architecture principles but doesn't affect functionality

### 📱 iOS Platform Support (Medium Priority)
- **Current State**: iOS builds successfully but won't run
- **Missing Component**: Koin dependency injection initialization in iOS app entry point
- **Required Action**: Add proper Koin setup in iOS main function
- **Impact**: iOS users cannot use the application until this is resolved

### 🧪 Testing Coverage
- **ViewModel Tests**: ✅ Comprehensive coverage with fake implementations
- **UseCase Tests**: ❌ Missing - high priority technical debt
- **Integration Tests**: Limited due to external API dependencies

## 💻 Development Setup

### Prerequisites
- **JDK 17+**
- **Android Studio** or **IntelliJ IDEA** with Kotlin Multiplatform plugin
- **Git** for version control

### 🚀 Quick Start

#### Clone & Build
```bash
# Clone the repository
git clone <repository-url>
cd NBP-Rates

# Build the Android application
./gradlew :composeApp:assembleDebug

# Install on connected device/emulator
./gradlew :composeApp:installDebug
```

#### Code Quality
```bash
# Auto-format code
./gradlew spotlessApply

# Run static analysis
./gradlew detekt

# Run all quality checks
./gradlew check
```

### 🧪 Testing

#### Run Tests
```bash
# Run all unit tests
./gradlew :feature:currency-list:currency-list-presentation:test
./gradlew :feature:currency-details:currency-details-presentation:test

# Build specific modules
./gradlew :feature:currency-list:build
./gradlew :feature:currency-details:build
```

#### Test Structure
- **Fake Implementations**: Deterministic test data for reliable unit tests
- **Test Isolation**: Fresh instances for each test
- **Flow Testing**: Turbine for proper StateFlow testing
- **Kotest Assertions**: Expressive and readable test expectations

## 📋 Build Commands

### Application Builds
```bash
# Android Debug APK
./gradlew :composeApp:assembleDebug

# Android Release APK
./gradlew :composeApp:assembleRelease

# iOS Build (requires Koin fix)
./gradlew :composeApp:assembleDebugIosX64

# Full Project Build
./gradlew build
```

### Module-Specific Builds
```bash
# Individual feature modules
./gradlew :feature:currency-list:build
./gradlew :feature:currency-details:build

# Specific layers
./gradlew :feature:currency-list:currency-list-presentation:build
./gradlew :feature:currency-details:currency-details-presentation:build
```

## 📏 Code Standards & Architecture Rules

### 🏗️ Architecture Principles
- **Feature-First Design**: Each feature is self-contained with all layers
- **Clean Layer Separation**: Domain → Network → Data → Presentation → DI
- **Public Interfaces**: Only expose necessary APIs between layers
- **Internal Implementations**: Hide implementation details within modules
- **Dependency Injection**: Use Koin for all dependency management
- **Error Handling**: Use Result<T> pattern consistently throughout the stack

### 📐 Feature Module Structure
Each feature follows this standardized pattern:
```
feature/{feature-name}/
├── {feature-name}-domain/       # Domain models + repository interfaces
├── {feature-name}-network/      # API service + DTOs (public/internal)
├── {feature-name}-data/         # Repository implementations
├── {feature-name}-presentation/ # ViewModels + UseCases + UI
└── {feature-name}-di/           # Centralized DI modules
```

### 🔧 UseCase Implementation
- **Current Location**: Temporarily in presentation layer (technical debt)
- **Interface**: Public visibility, defines business logic contracts
- **Implementation**: Internal visibility, contains business logic
- **Testing**: Must have comprehensive unit tests (currently missing)
- **Future Goal**: Move to dedicated use-case layer

### 📝 Code Quality Standards
- **Zero Critical Issues**: Detekt static analysis enforcement
- **Consistent Formatting**: Spotless with ktlint code formatting
- **120 Character Line Limit**: Maintain code readability
- **Comprehensive Documentation**: KDoc for all public APIs
- **Test Coverage**: Fake implementations with proper test isolation

### 🔄 Visibility Modifiers
- **Public**: Interfaces, Screens, API contracts, UseCase interfaces
- **Internal**: Implementations, ViewModels, Services, UseCase implementations
- **Package-private**: Helper classes within same package

### 🧪 Testing Standards
- **Fake Implementations**: Use deterministic test data for reliable tests
- **Test Isolation**: Fresh instances for each test to avoid interference
- **Flow Testing**: Use Turbine for StateFlow and coroutine testing
- **Assertion Style**: Kotest with expressive, readable expectations

### 📋 Git Workflow
- **Conventional Commits**: Clear, descriptive commit messages (feat:, fix:, docs:, refactor:, test:)
- **No Co-Authored-By**: Per project configuration settings
- **Quality Gates**: All checks (detekt, spotless, tests) must pass before merge
- **Branch Strategy**: Feature branches for development, main for production

## 🔍 Current Implementation Status

### ✅ Completed Features
- **Currency List Module**: Full implementation with parallel API calls
- **Currency Details Module**: Complete with historical data and charts
- **Core Infrastructure**: Network factory, DI setup, quality tools
- **Architecture**: Feature-specific modular pattern established
- **Testing Framework**: Comprehensive unit tests with fake implementations
- **Code Quality**: Clean code practices with proper tooling

### 🎯 Next Steps
1. **Create UseCase Layer**: Move UseCases to dedicated architectural layer
2. **Add UseCase Tests**: Comprehensive unit test coverage for all UseCases
3. **Fix iOS Support**: Implement Koin initialization for iOS platform
4. **Enhance Testing**: Add integration tests with proper mocking

## 📞 Support & Contributing

### Getting Help
- Check existing documentation and code comments
- Review test files for usage patterns
- Follow established architectural patterns

### Contributing Guidelines
- Follow feature-specific architecture
- Maintain code quality standards
- Add comprehensive tests for new features
- Update documentation for API changes

---

**Built with ❤️ using Kotlin Multiplatform and Jetpack Compose**