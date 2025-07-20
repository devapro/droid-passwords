package io.github.devapro

import android.app.Application
import io.github.devapro.data.AndroidThemeDetector
import io.github.devapro.di.initKoin

class DroidPasswordApplication: Application() {

    companion object {
        lateinit var instance: DroidPasswordApplication
            private set
    }
    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize theme detector with application context
        AndroidThemeDetector.initialize(this)
        
        initKoin()
    }
}