package io.github.devapro.droid.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

expect fun createLocalStorageDataStore(): DataStore<Preferences>

class LocalStorage {

    private val dataStore = createLocalStorageDataStore()

    suspend fun saveString(key: String, value: String) {
        dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    fun getString(key: String): Flow<String> {
        return dataStore.data.map {
            it[stringPreferencesKey(key)] ?: ""
        }
    }
}