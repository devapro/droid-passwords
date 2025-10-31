package io.github.devapro.droid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual fun createLocalStorageDataStore(): DataStore<Preferences> {
    return object : KoinComponent {
        val context: Context by inject()
    }.let { koin ->
        createDataStore(
            producePath = {
                koin.context.filesDir.resolve("app.preferences_pb").absolutePath
            }
        )
    }
}
