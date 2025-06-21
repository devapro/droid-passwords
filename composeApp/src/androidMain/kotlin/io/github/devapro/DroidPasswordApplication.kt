package io.github.devapro

import android.app.Application
import io.github.devapro.di.initKoin

class DroidPasswordApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}