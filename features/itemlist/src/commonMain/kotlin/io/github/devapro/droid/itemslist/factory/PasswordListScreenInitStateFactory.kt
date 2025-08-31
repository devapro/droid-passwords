package io.github.devapro.droid.itemslist.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.droid.itemslist.model.PasswordListScreenState

class PasswordListScreenInitStateFactory : InitStateFactory<PasswordListScreenState> {

    override fun createInitState(): PasswordListScreenState {
        return PasswordListScreenState.Loading
    }
} 