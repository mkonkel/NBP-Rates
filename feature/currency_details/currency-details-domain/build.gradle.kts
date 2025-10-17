plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
