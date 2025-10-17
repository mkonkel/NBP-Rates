plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.currencyList.currencyListDomain)
            implementation(projects.feature.currencyList.currencyListNetwork)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
