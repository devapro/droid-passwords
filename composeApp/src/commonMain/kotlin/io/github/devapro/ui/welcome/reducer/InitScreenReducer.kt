package io.github.devapro.ui.welcome.reducer

import io.github.devapro.core.mvi.Reducer
import io.github.devapro.ui.welcome.model.WelcomeScreenAction
import io.github.devapro.ui.welcome.model.WelcomeScreenEvent
import io.github.devapro.ui.welcome.model.WelcomeScreenState

class InitScreenReducer
    : Reducer<WelcomeScreenAction.InitScreen, WelcomeScreenState, WelcomeScreenAction, WelcomeScreenEvent> {

    override val actionClass = WelcomeScreenAction.InitScreen::class

    override suspend fun reduce(
        action: WelcomeScreenAction.InitScreen,
        getState: () -> WelcomeScreenState
    ): Reducer.Result<WelcomeScreenState, WelcomeScreenAction.InitScreen, WelcomeScreenEvent?> {
        return Reducer.Result(
            state = WelcomeScreenState.Success(
                platformName = "DroidPasswords",
                isDarkThemeEnabled = false,
                isBiometricAuthEnabled = false,
                isPasswordProtected = false
            ),
            action = action,
            event = null
        )
    }
}