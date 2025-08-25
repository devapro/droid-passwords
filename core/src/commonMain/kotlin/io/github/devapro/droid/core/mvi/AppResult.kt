package io.github.devapro.droid.core.mvi

sealed class AppResult<T : Any?> {

    data class Success<T : Any?>(val value: T) : AppResult<T>()

    data class Failure<T : Any?>(val error: Exception) : AppResult<T>()
}
