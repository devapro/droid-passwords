package io.github.devapro

import android.app.Application
import io.github.devapro.di.appModule
import io.github.devapro.droid.data.AndroidThemeDetector
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DroidPasswordApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin with Android context
        startKoin {
            androidContext(this@DroidPasswordApplication)
            modules(appModule)
        }

        // Initialize theme detector with application context
        AndroidThemeDetector.initialize(this)
    }
}