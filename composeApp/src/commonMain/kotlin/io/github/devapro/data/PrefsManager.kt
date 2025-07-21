package io.github.devapro.data

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalStorage {

    private val dataStore = createDataStore { "app.preferences_pb" }

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