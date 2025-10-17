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
            baseName = "Database"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            // Room database dependencies will be added here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
