plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Domain"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Pure domain layer - no external dependencies
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
