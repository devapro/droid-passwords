package io.github.devapro.features.itemslist.factory

import io.github.devapro.droid.core.mvi.InitStateFactory
import io.github.devapro.features.itemslist.model.PasswordListScreenState

class PasswordListScreenInitStateFactory : InitStateFactory<PasswordListScreenState> {

    override fun createInitState(): PasswordListScreenState {
        return PasswordListScreenState.Loading
    }
} 