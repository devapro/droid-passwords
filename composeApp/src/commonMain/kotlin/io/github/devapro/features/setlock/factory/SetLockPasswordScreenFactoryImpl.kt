package io.github.devapro.features.setlock.factory

import androidx.compose.runtime.Composable
import io.github.devapro.droid.setlock.SetLockPasswordScreenFactory
import io.github.devapro.features.setlock.SetLockPasswordScreenRoot

class SetLockPasswordScreenFactoryImpl: SetLockPasswordScreenFactory {

    @Composable
    override fun CreateSetLockPasswordScreen() {
        SetLockPasswordScreenRoot()
    }
}