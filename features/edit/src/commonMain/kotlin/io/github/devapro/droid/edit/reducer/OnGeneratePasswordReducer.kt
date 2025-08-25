package io.github.devapro.droid.edit.reducer

import io.github.devapro.droid.core.mvi.Reducer
import io.github.devapro.droid.edit.model.AddEditPasswordScreenAction
import io.github.devapro.droid.edit.model.AddEditPasswordScreenEvent
import io.github.devapro.droid.edit.model.AddEditPasswordScreenState
import kotlin.random.Random

class OnGeneratePasswordReducer
    : Reducer<AddEditPasswordScreenAction.OnGeneratePassword, AddEditPasswordScreenState, AddEditPasswordScreenAction, AddEditPasswordScreenEvent> {

    override val actionClass = AddEditPasswordScreenAction.OnGeneratePassword::class

    override suspend fun reduce(
        action: AddEditPasswordScreenAction.OnGeneratePassword,
        getState: () -> AddEditPasswordScreenState
    ): Reducer.Result<AddEditPasswordScreenState, AddEditPasswordScreenAction.OnGeneratePassword, AddEditPasswordScreenEvent?> {
        val currentState = getState()

        return if (currentState is AddEditPasswordScreenState.Success) {
            val generatedPassword = generatePassword()
            val isFormValid = currentState.title.isNotBlank() && generatedPassword.isNotBlank()
            
            Reducer.Result(
                state = currentState.copy(
                    password = generatedPassword,
                    passwordError = null,
                    isFormValid = isFormValid
                ),
                action = null,
                event = AddEditPasswordScreenEvent.GeneratedPassword(generatedPassword)
            )
        } else {
            Reducer.Result(
                state = currentState,
                action = null,
                event = null
            )
        }
    }
    
    private fun generatePassword(length: Int = 16): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }
} 