plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Network"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
