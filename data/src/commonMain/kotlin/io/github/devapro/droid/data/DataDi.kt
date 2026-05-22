package io.github.devapro.droid.data

import io.github.devapro.droid.data.vault.CryptoMapper
import io.github.devapro.droid.data.vault.VaultFileRepository
import io.github.devapro.droid.data.vault.VaultRegistryRepository
import io.github.devapro.droid.data.vault.VaultRuntimeRepository
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf

fun Module.registerDataDi() {
    single { LocalStorage() }
    single {
        LockManager.also { it.attachRuntimeRepository(get()) }
    }
    factoryOf(::VaultFileRepository)
    singleOf(::VaultRuntimeRepository)
    singleOf(::VaultRegistryRepository)
    singleOf(::CryptoMapper)
    singleOf(::ThemeManager)
    single {
        Json { ignoreUnknownKeys = true }
    }
}
