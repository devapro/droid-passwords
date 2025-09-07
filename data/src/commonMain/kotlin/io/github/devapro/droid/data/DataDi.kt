package io.github.devapro.droid.data

import io.github.devapro.droid.data.vault.CryptoMapper
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf

fun Module.registerDataDi() {
    factoryOf(::LocalStorage)
    single { LockManager }
    factoryOf(::VaultFileRepository)
    singleOf(::VaultRuntimeRepository)
    singleOf(::CryptoMapper)
    singleOf(::ThemeManager)
    single {
        Json { ignoreUnknownKeys = true }
    }
}