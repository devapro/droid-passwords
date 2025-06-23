package io.github.devapro.core.mvi

sealed class AppResult<T : Any?> {

    data class Success<T : Any?>(val value: T) : AppResult<T>()

    data class Failure<T : Any?>(val error: Exception) : AppResult<T>()
}
