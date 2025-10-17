package dev.michalkonkel.nbp

import android.app.Application
import dev.michalkonkel.nbp.di.initKoin

/**
 * Application class for NBP Rates app.
 * Initializes Koin dependency injection.
 */
class NbpApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}
