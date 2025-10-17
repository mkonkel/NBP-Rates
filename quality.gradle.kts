plugins {
    id("io.gitlab.arturbosch.detekt")
    id("com.diffplug.spotless")
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
        ktlint()
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        ktlint()
    }
}
