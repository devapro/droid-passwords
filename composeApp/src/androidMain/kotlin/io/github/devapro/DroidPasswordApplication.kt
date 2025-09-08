package io.github.devapro

import android.app.Application
import io.github.devapro.di.initKoin
import io.github.devapro.droid.data.AndroidThemeDetector

class DroidPasswordApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()

        // Initialize theme detector with application context
        AndroidThemeDetector.initialize(this)
    }
}