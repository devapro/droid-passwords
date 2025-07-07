package io.github.devapro.core.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import io.github.devapro.core.mvi.CoroutineContextProvider
import kotlinx.coroutines.launch

class SnackbarHostStateManager(
    coroutineContextProvider: CoroutineContextProvider
) {

    private val scope = coroutineContextProvider.createScope(coroutineContextProvider.default)
    val state = SnackbarHostState()

    fun show(
        message: String,
        actionButtonText: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        actionButtonCallback: (() -> Unit)? = null
    ) {
        scope.launch {
            val result = state.showSnackbar(
                message = message,
                actionLabel = actionButtonText,
                duration = duration
            )
            when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> actionButtonCallback?.invoke()
            }
        }
    }
}