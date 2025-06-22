package io.github.devapro

import android.app.Application
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

        initKoin()
    }
}