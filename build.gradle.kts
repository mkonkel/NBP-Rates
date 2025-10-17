plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

detekt {
    toolVersion = "1.23.7"
    source.setFrom(files("src"))
    config.setFrom(files("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    autoCorrect = true
}

spotless {
    kotlin {
        target("**/*.kt", "**/*.kts")
        targetExclude("**/iosMain/**")
        ktlint()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
