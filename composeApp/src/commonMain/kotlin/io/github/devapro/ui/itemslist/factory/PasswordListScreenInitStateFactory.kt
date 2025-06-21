package io.github.devapro.ui.itemslist.factory

import io.github.devapro.core.mvi.InitStateFactory
import io.github.devapro.ui.itemslist.model.PasswordListScreenState

class PasswordListScreenInitStateFactory : InitStateFactory<PasswordListScreenState> {

    override fun createInitState(): PasswordListScreenState {
        return PasswordListScreenState.Loading
    }
} 