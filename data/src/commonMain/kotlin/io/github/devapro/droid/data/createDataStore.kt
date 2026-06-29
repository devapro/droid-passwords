package io.github.devapro.droid.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

private val dataStoreCache = mutableMapOf<String, DataStore<Preferences>>()

/**
 * Returns a singleton [DataStore] for the given file path. DataStore must not be
 * opened more than once per file — callers share the cached instance.
 */
fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    val path = producePath()
    return synchronized(dataStoreCache) {
        dataStoreCache.getOrPut(path) {
            PreferenceDataStoreFactory.createWithPath(
                produceFile = { path.toPath() }
            )
        }
    }
}

internal const val dataStoreFileName = "settings.plist"