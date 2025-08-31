package io.github.devapro.droid.itemdetails.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.itemdetails.model.PasswordDetailScreenState

class PasswordDetailScreenInitStateFactory : InitStateFactory<PasswordDetailScreenState> {

    override fun createInitState(): PasswordDetailScreenState {
        return PasswordDetailScreenState.Loading
    }
} 